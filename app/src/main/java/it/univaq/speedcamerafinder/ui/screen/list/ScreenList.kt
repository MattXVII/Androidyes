package it.univaq.speedcamerafinder.ui.screen.list

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import it.univaq.speedcamerafinder.common.distanceFrom
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.PermissionGate

@Composable
fun ScreenList(
    viewModel: ListViewModel = hiltViewModel(),
    onItemClick: (SpeedCamera) -> Unit = {}
) {
    val uiState = viewModel.uiState

    PermissionGate(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    ) {
        val localLifecycle = LocalLifecycleOwner.current
        DisposableEffect(localLifecycle) {
            val observer = LifecycleEventObserver { _, event ->
                when(event) {
                    Lifecycle.Event.ON_RESUME -> viewModel.onEvent(ListUiEvent.StartLocation)
                    Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(ListUiEvent.StopLocation)
                    else -> {}
                }
            }
            localLifecycle.lifecycle.addObserver(observer)

            // Quando si cambia schermata fermo anche il GPS
            onDispose {
                localLifecycle.lifecycle.removeObserver(observer)
                viewModel.onEvent(ListUiEvent.StopLocation)
            }
        }
    }

    ListContent(
        uiState = uiState,
        onItemClick = onItemClick
    )
}

@Composable
private fun ListContent(
    uiState: ListUiState = ListUiState(),
    onItemClick: (SpeedCamera) -> Unit = {}
) {
    val items = uiState.items
    val location = uiState.location

    if (location == null) {
        CenteredMessage("In attesa della posizione...")
        return
    }
    if (uiState.isLoading) {
        CenteredMessage("Caricamento...")
        return
    }
    if (uiState.error != null) {
        CenteredMessage("Errore: ${uiState.error}")
        return
    }
    if (items.isEmpty()) {
        CenteredMessage("Nessun autovelox nelle vicinanze")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            val km = items[index].distanceFrom(location) / 1000
            ListItem(
                title = items[index].maxSpeed?.let { "Limite $it km/h" } ?: "Limite non indicato",
                subtitle = "A %.1f km da te".format(km),
                onItemClick = {
                    onItemClick(items[index])
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
    title: String = "Title",
    subtitle: String = "Subtitle",
    onItemClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            style = typography.titleMedium
        )
        Text(
            text = subtitle,
            modifier = Modifier.fillMaxWidth(),
            style = typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun ListContentPreview() {
    ListContent()
}
