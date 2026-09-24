package it.univaq.speedcamerafinder.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Cartello del limite di velocità: cerchio bianco con il bordo rosso
@Preview
@Composable
fun SpeedLimitSign(
    maxSpeed: Int? = 50,
    size: Dp = 48.dp
) {
    Box(
        modifier = Modifier.size(size)
            .background(Color.White, CircleShape)
            .border(size / 10, Color.Red, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = maxSpeed?.toString() ?: "?",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style = typography.titleMedium
        )
    }
}
