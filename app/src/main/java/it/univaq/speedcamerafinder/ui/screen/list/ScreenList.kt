package it.univaq.speedcamerafinder.ui.screen.list

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import it.univaq.speedcamerafinder.domain.model.SpeedCamera

@Composable
fun ScreenList(
    viewModel: ListViewModel = hiltViewModel(),
    onItemClick: (SpeedCamera) -> Unit = {}
) {
    val uiState = viewModel.uiState
    ListContent(
        items = uiState.items,
        onItemClick = onItemClick
    )
}

@Composable
private fun ListContent(
    items: List<SpeedCamera> = emptyList(),
    onItemClick: (SpeedCamera) -> Unit = {}
) {
    if (items.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No items")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            ListItem(
                title = "Autovelox",
                subtitle = items[index].maxSpeed?.let { "Limite $it km/h" } ?: "Limite non indicato",
                onItemClick = {
                    onItemClick(items[index])
                }
            )
        }
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
