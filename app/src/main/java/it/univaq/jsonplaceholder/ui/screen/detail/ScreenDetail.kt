package it.univaq.jsonplaceholder.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import it.univaq.jsonplaceholder.domain.model.User

@Composable
fun ScreenDetail(
    user: User
) {
    Column {
        Text(
            text = user.name,
        )
        Text(
            text = user.city,
        )
        Text(
            text = user.username,
        )
        Text(
            text = user.email,
        )
    }
}