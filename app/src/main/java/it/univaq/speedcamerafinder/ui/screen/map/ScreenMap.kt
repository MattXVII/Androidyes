package it.univaq.speedcamerafinder.ui.screen.map

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.PermissionGate

@Composable
fun ScreenMap(
    onItemClick: (SpeedCamera) -> Unit = {},
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val cameraPositionState = rememberCameraPositionState()

    // Quando arriva la posizione centro la mappa sull'utente
    LaunchedEffect(uiState.location != null) {
        uiState.location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }

    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        uiState.items.forEach { camera ->
            Marker(
                state = rememberUpdatedMarkerState(LatLng(camera.lat, camera.lng)),
                title = "Autovelox",
                snippet = camera.maxSpeed?.let { "Limite $it km/h" },
                onInfoWindowClick = {
                    onItemClick(camera)
                }
            )
        }

        PermissionGate (
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {

            val localLifecycle = LocalLifecycleOwner.current
            DisposableEffect(localLifecycle) {
                val observer = LifecycleEventObserver { _, event ->
                    when(event) {
                        Lifecycle.Event.ON_RESUME -> viewModel.onEvent(MapUiEvent.StartLocation)
                        Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(MapUiEvent.StopLocation)
                        else -> {}
                    }
                }
                localLifecycle.lifecycle.addObserver(observer)

                // Quando si cambia schermata fermo anche il GPS
                onDispose {
                    localLifecycle.lifecycle.removeObserver(observer)
                    viewModel.onEvent(MapUiEvent.StopLocation)
                }
            }

            uiState.location?.let {
                Marker(
                    state = rememberUpdatedMarkerState(it),
                    title = "La mia posizione",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }
    }
}