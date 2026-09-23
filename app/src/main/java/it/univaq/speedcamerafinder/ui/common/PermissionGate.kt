package it.univaq.speedcamerafinder.ui.common

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState

val LOCATION_PERMISSIONS = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGate(
    permissions: List<String> = emptyList(),
    onPermissionsAllowed: @Composable () -> Unit = {},
) {
    if (permissions.isEmpty()) {
        onPermissionsAllowed()
        return
    }

    // Diventa true dopo la prima risposta dell'utente alla richiesta di sistema
    var alreadyAsked by rememberSaveable { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(permissions) {
        alreadyAsked = true
    }

    // Basta un permesso: con la sola posizione approssimativa l'app funziona lo stesso
    if (permissionState.permissions.any { it.status.isGranted }) {
        onPermissionsAllowed()
    } else {

        if (permissionState.shouldShowRationale) {

            val isDialogVisible = remember { mutableStateOf(true) }
            if (isDialogVisible.value) {
                PermissionDialog(
                    title = "Permesso necessario",
                    message = "Serve la posizione per trovare gli autovelox vicini",
                    onDismiss = {
                        isDialogVisible.value = false
                    },
                    onConfirm = {
                        permissionState.launchMultiplePermissionRequest()
                    }
                )
            }

        } else if (alreadyAsked) {

            // Negato due volte: Android non mostra più la richiesta,
            // l'utente può riattivare il permesso solo dalle impostazioni
            val context = LocalContext.current
            val isDialogVisible = remember { mutableStateOf(true) }
            if (isDialogVisible.value) {
                PermissionDialog(
                    title = "Permesso negato",
                    message = "Attiva la posizione dalle impostazioni dell'app",
                    confirmText = "Impostazioni",
                    onDismiss = {
                        isDialogVisible.value = false
                    },
                    onConfirm = {
                        isDialogVisible.value = false
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    }
                )
            }

        } else {
            SideEffect {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    title: String = "Title",
    message: String = "Message",
    confirmText: String = "Richiedi",
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = title,
                    style = typography.titleMedium
                )
                Text(
                    text = message,
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                    ) {
                        Text("Annulla")
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}