package it.univaq.speedcamerafinder.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.univaq.speedcamerafinder.domain.model.SpeedCamera

@Composable
fun ScreenDetail(
    camera: SpeedCamera
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Autovelox",
            style = typography.titleLarge
        )
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
