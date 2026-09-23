package it.univaq.speedcamerafinder.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import it.univaq.speedcamerafinder.common.LocationHelper
import it.univaq.speedcamerafinder.common.Result
import it.univaq.speedcamerafinder.common.distanceFrom
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.usecase.GetSpeedCamerasUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SpeedCameraUiState (
    val items: List<SpeedCamera> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: LatLng? = null
)

sealed class SpeedCameraUiEvent {
    data object StartLocation: SpeedCameraUiEvent()
    data object StopLocation: SpeedCameraUiEvent()
    data object Refresh: SpeedCameraUiEvent()
}

// Unico ViewModel condiviso da lista e mappa: stessi dati, stesso ordine
@HiltViewModel
class SpeedCameraViewModel @Inject constructor(
    private val getSpeedCamerasUseCase: GetSpeedCamerasUseCase,
    private val locationHelper: LocationHelper
): ViewModel() {

    var uiState by mutableStateOf(SpeedCameraUiState())
        private set

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return
            // Scarico gli autovelox solo alla prima posizione ricevuta
            val isFirstLocation = uiState.location == null
            uiState = uiState.copy(location = LatLng(location.latitude, location.longitude))
            if (isFirstLocation) load(location.latitude, location.longitude)
        }
    }

    fun onEvent(event: SpeedCameraUiEvent) {
        when(event) {
            is SpeedCameraUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is SpeedCameraUiEvent.StopLocation -> locationHelper.stop(locationCallback)
            // Riscarico gli autovelox intorno alla posizione attuale
            is SpeedCameraUiEvent.Refresh -> uiState.location?.let { load(it.latitude, it.longitude) }
        }
    }

    private fun load(lat: Double, lng: Double) {
        viewModelScope.launch {
            getSpeedCamerasUseCase(lat, lng).collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true, error = null)
                    is Result.Success -> uiState.copy(
                        // Dal più vicino al più lontano
                        items = it.data.sortedBy { camera -> camera.distanceFrom(LatLng(lat, lng)) },
                        isLoading = false
                    )
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)
                }
            }
        }
    }
}
