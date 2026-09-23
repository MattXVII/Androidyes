package it.univaq.jsonplaceholder.ui.common

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

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

    val permissionState = rememberMultiplePermissionsState(permissions)
    if (permissionState.allPermissionsGranted) {
        onPermissionsAllowed()
    } else {

        if (permissionState.shouldShowRationale) {

            val isDialogVisible = remember { mutableStateOf(true) }
            if (isDialogVisible.value) {
                PermissionDialog(
                    title = "Permission required",
                    message = "Permission required to show my location on map",
                    onDismiss = {
                        isDialogVisible.value = false
                    },
                    onConfirm = {
                        permissionState.launchMultiplePermissionRequest()
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
                        Text("Cancel")
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Request")
                    }
                }
            }
        }
    }
}