package it.univaq.speedcamerafinder.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.SpeedLimitSign
import it.univaq.speedcamerafinder.ui.screen.map.markerColor

@Composable
fun ScreenDetail(
    camera: SpeedCamera,
    number: Int
) {
    val cameraLatLng = LatLng(camera.lat, camera.lng)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Mini-mappa centrata sull'autovelox
        GoogleMap(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(cameraLatLng, 16f)
            }
        ) {
            Marker(
                state = rememberUpdatedMarkerState(cameraLatLng),
                title = "Autovelox $number",
                icon = markerColor(camera.maxSpeed)
            )
        }

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpeedLimitSign(camera.maxSpeed, 64.dp)
            Text(
                text = "Autovelox $number",
                style = typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = camera.maxSpeed?.let { "Limite: $it km/h" } ?: "Limite non indicato",
            )
            Text(
                text = camera.direction?.let { "Direzione: $it°" } ?: "Direzione non indicata",
            )
            Text(
                text = "Coordinate: ${camera.lat}, ${camera.lng}",
            )
            Text(
                text = "ID OpenStreetMap: ${camera.id}",
                style = typography.bodySmall
            )
            // Attribuzione richiesta dalla licenza ODbL dei dati OpenStreetMap
            Text(
                text = "Dati © OpenStreetMap contributors",
                style = typography.bodySmall
            )
        }
    }
}
