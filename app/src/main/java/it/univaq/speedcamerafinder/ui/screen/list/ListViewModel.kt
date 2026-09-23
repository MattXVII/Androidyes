package it.univaq.speedcamerafinder.ui.screen.list

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
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.usecase.GetSpeedCamerasUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListUiState (
    val items: List<SpeedCamera> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: LatLng? = null
)

sealed class ListUiEvent {
    data object StartLocation: ListUiEvent()
    data object StopLocation: ListUiEvent()
}

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getSpeedCamerasUseCase: GetSpeedCamerasUseCase,
    private val locationHelper: LocationHelper
): ViewModel() {

    var uiState by mutableStateOf(ListUiState())
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

    fun onEvent(event: ListUiEvent) {
        when(event) {
            is ListUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is ListUiEvent.StopLocation -> locationHelper.stop(locationCallback)
        }
    }

    private fun load(lat: Double, lng: Double) {
        viewModelScope.launch {
            getSpeedCamerasUseCase(lat, lng).collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)
                }
            }
        }
    }
}
