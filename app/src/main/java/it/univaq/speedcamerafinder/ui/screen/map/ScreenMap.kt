package it.univaq.speedcamerafinder.ui.screen.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
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

    Box(modifier = Modifier.fillMaxSize()) {
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
                    icon = markerColor(camera.maxSpeed),
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

        MapLegend(
            modifier = Modifier.align(Alignment.TopCenter).padding(8.dp)
        )
    }
}

// Fasce di limite: dal colore più tenue (velocità bassa) al più forte (velocità alta)
private fun markerHue(maxSpeed: Int?): Float = when {
    maxSpeed == null -> BitmapDescriptorFactory.HUE_AZURE
    maxSpeed <= 50 -> BitmapDescriptorFactory.HUE_YELLOW
    maxSpeed <= 90 -> BitmapDescriptorFactory.HUE_ORANGE
    else -> BitmapDescriptorFactory.HUE_RED
}

fun markerColor(maxSpeed: Int?): BitmapDescriptor =
    BitmapDescriptorFactory.defaultMarker(markerHue(maxSpeed))

// Legenda semi-trasparente: usa gli stessi colori dei marker
@Preview
@Composable
private fun MapLegend(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "km/h", style = typography.labelMedium)
            LegendItem(markerHue(50), "≤ 50")
            LegendItem(markerHue(90), "51-90")
            LegendItem(markerHue(130), "> 90")
            LegendItem(markerHue(null), "N/D")
        }
    }
}

@Composable
private fun LegendItem(hue: Float, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(12.dp)
                .background(Color.hsv(hue, 1f, 1f), CircleShape)
        )
        Text(
            text = text,
            style = typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
