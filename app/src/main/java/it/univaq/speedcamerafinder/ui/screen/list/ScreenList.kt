package it.univaq.speedcamerafinder.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.univaq.speedcamerafinder.common.distanceFrom
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.LOCATION_PERMISSIONS
import it.univaq.speedcamerafinder.ui.common.PermissionGate
import it.univaq.speedcamerafinder.ui.common.SpeedLimitSign
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraUiEvent
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraUiState
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraViewModel

@Composable
fun ScreenList(
    viewModel: SpeedCameraViewModel,
    isLocationGranted: Boolean,
    onItemClick: (SpeedCamera, Int) -> Unit = { _, _ -> }
) {
    val uiState = viewModel.uiState

    // Ogni click sul tasto ricrea PermissionGate, che quindi rifà la richiesta
    var permissionRequests by rememberSaveable { mutableIntStateOf(0) }
    key(permissionRequests) {
        PermissionGate(permissions = LOCATION_PERMISSIONS)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Con il permesso già concesso il tasto non è più cliccabile
            Button(
                onClick = { permissionRequests++ },
                enabled = !isLocationGranted,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null)
                Text(
                    text = if (isLocationGranted) "Posizione attiva" else "Attiva posizione",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.onEvent(SpeedCameraUiEvent.Refresh) },
                enabled = uiState.location != null && !uiState.isLoading,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Text(
                    text = "Aggiorna",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        ListContent(
            uiState = uiState,
            isLocationGranted = isLocationGranted,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun ListContent(
    uiState: SpeedCameraUiState = SpeedCameraUiState(),
    isLocationGranted: Boolean = false,
    onItemClick: (SpeedCamera, Int) -> Unit = { _, _ -> }
) {
    val items = uiState.items
    val location = uiState.location

    if (!isLocationGranted) {
        CenteredMessage("Attiva la posizione per trovare gli autovelox vicini")
        return
    }
    if (location == null) {
        CenteredMessage("In attesa della posizione...")
        return
    }
    if (uiState.isLoading && items.isEmpty()) {
        CenteredMessage("Caricamento...")
        return
    }
    if (items.isEmpty() && uiState.error != null) {
        CenteredMessage("Errore: ${uiState.error}")
        return
    }
    if (items.isEmpty()) {
        CenteredMessage("Nessun autovelox nelle vicinanze")
        return
    }

    // Aggiornamento in corso: intanto lascio visibili gli autovelox già caricati
    if (uiState.isLoading) {
        Text(
            text = "I dati si stanno aggiornando, attendi...",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    // Download fallito ma ci sono i dati salvati in Room: li mostro avvisando l'utente
    if (uiState.error != null) {
        Text(
            text = "Dati non aggiornati (${uiState.error}). Premi Aggiorna per riprovare.",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            // Numero = posizione nella lista (dal più vicino): è lo stesso usato sulla mappa
            val number = index + 1
            val km = items[index].distanceFrom(location) / 1000
            ListItem(
                title = "Autovelox $number",
                subtitle = "A %.1f km da te".format(km),
                maxSpeed = items[index].maxSpeed,
                onItemClick = {
                    onItemClick(items[index], number)
                }
            )
        }
    }
}

@Composable
private fun CenteredMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}

@Preview
@Composable
private fun ListItem(
    title: String = "Autovelox 1",
    subtitle: String = "A 1.2 km da te",
    maxSpeed: Int? = 50,
    onItemClick: () -> Unit = {}
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpeedLimitSign(maxSpeed)
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun ListContentPreview() {
    ListContent()
}
