package it.univaq.speedcamerafinder.ui.screen.map

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
import it.univaq.speedcamerafinder.domain.model.User
import it.univaq.speedcamerafinder.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState (
    val items: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val location: LatLng? = null
)

sealed class MapUiEvent {
    data object StartLocation: MapUiEvent()
    data object StopLocation: MapUiEvent()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val locationHelper: LocationHelper
): ViewModel() {

    var uiState by mutableStateOf(MapUiState())
        private set

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation ?: return
            uiState = uiState.copy(location = LatLng(location.latitude, location.longitude))
        }
    }

    init {
        load()
    }

    fun onEvent(event: MapUiEvent) {
        when(event) {
            is MapUiEvent.StartLocation -> locationHelper.start(locationCallback)
            is MapUiEvent.StopLocation -> locationHelper.stop(locationCallback)
        }
    }

    private fun load() {
        viewModelScope.launch {
            getUsersUseCase().collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)
                }
            }
        }
    }
}