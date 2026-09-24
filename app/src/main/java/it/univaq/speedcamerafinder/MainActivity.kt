package it.univaq.speedcamerafinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.ui.common.LOCATION_PERMISSIONS
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraUiEvent
import it.univaq.speedcamerafinder.ui.screen.SpeedCameraViewModel
import it.univaq.speedcamerafinder.ui.screen.detail.ScreenDetail
import it.univaq.speedcamerafinder.ui.screen.list.ScreenList
import it.univaq.speedcamerafinder.ui.screen.map.ScreenMap
import it.univaq.speedcamerafinder.ui.theme.SpeedCameraFinderTheme

data object ListScreen
data object MapScreen
data class DetailScreen(val camera: SpeedCamera, val number: Int)

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedCameraFinderTheme {
                val backStack = remember { mutableStateListOf<Any>(ListScreen) }

                // Un solo ViewModel per lista e mappa
                val viewModel: SpeedCameraViewModel = hiltViewModel()

                val permissionState = rememberMultiplePermissionsState(LOCATION_PERMISSIONS)
                val isLocationGranted = permissionState.permissions.any { it.status.isGranted }

                // GPS acceso solo con il permesso concesso e l'app in primo piano
                if (isLocationGranted) {
                    val localLifecycle = LocalLifecycleOwner.current
                    DisposableEffect(localLifecycle) {
                        val observer = LifecycleEventObserver { _, event ->
                            when(event) {
                                Lifecycle.Event.ON_RESUME -> viewModel.onEvent(SpeedCameraUiEvent.StartLocation)
                                Lifecycle.Event.ON_PAUSE -> viewModel.onEvent(SpeedCameraUiEvent.StopLocation)
                                else -> {}
                            }
                        }
                        localLifecycle.lifecycle.addObserver(observer)

                        onDispose {
                            localLifecycle.lifecycle.removeObserver(observer)
                            viewModel.onEvent(SpeedCameraUiEvent.StopLocation)
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = backStack.lastOrNull() is ListScreen,
                                // Svuoto il back stack: premendo più volte le tab non si accumulano schermate
                                onClick = {
                                    backStack.clear()
                                    backStack.add(ListScreen)
                                },
                                icon = {
                                    Icon(Icons.Default.Home, contentDescription = "Lista")
                                },
                                label = { Text("Lista") }
                            )

                            NavigationBarItem(
                                selected = backStack.lastOrNull() is MapScreen,
                                // Dalla mappa il tasto indietro riporta alla lista
                                onClick = {
                                    backStack.clear()
                                    backStack.add(ListScreen)
                                    backStack.add(MapScreen)
                                },
                                icon = {
                                    Icon(Icons.Default.Place, contentDescription = "Mappa")
                                },
                                label = { Text("Mappa") }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = entryProvider {

                            entry<ListScreen> {
                                ScreenList(
                                    viewModel = viewModel,
                                    isLocationGranted = isLocationGranted,
                                    onItemClick = { camera, number ->
                                        backStack.add(DetailScreen(camera, number))
                                    }
                                )
                            }
                            entry<MapScreen> {
                                ScreenMap(
                                    viewModel = viewModel,
                                    onItemClick = { camera, number ->
                                        backStack.add(DetailScreen(camera, number))
                                    }
                                )
                            }
                            entry<DetailScreen> {
                                ScreenDetail(it.camera, it.number)
                            }
                        }
                    )
                }
            }
        }
    }
}
