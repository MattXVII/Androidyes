package it.univaq.speedcamerafinder.ui.screen.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.LOCATION_PERMISSIONS
import it.univaq.speedcamerafinder.ui.common.PermissionGate
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraViewModel

@Composable
fun ScreenMap(
    viewModel: SpeedCameraViewModel,
    onItemClick: (SpeedCamera, Int) -> Unit = { _, _ -> }
) {
    val uiState = viewModel.uiState
    val cameraPositionState = rememberCameraPositionState()

    // Quando arriva la posizione centro la mappa sull'utente
    LaunchedEffect(uiState.location != null) {
        uiState.location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 12f)
        }
    }

    PermissionGate(permissions = LOCATION_PERMISSIONS)

    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ){
        // Stessa lista e stesso ordine della schermata Lista: i numeri coincidono
        uiState.items.forEachIndexed { index, camera ->
            val number = index + 1
            Marker(
                state = rememberUpdatedMarkerState(LatLng(camera.lat, camera.lng)),
                title = "Autovelox $number",
                snippet = camera.maxSpeed?.let { "Limite $it km/h" },
                onInfoWindowClick = {
                    onItemClick(camera, number)
                }
            )
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
