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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import it.univaq.speedcamerafinder.domain.model.User
import it.univaq.speedcamerafinder.ui.screen.detail.ScreenDetail
import it.univaq.speedcamerafinder.ui.screen.list.ScreenList
import it.univaq.speedcamerafinder.ui.screen.map.ScreenMap
import it.univaq.speedcamerafinder.ui.theme.SpeedCameraFinderTheme

data object ListScreen
data object MapScreen
data class DetailScreen(val user: User)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedCameraFinderTheme {
                val backStack = remember { mutableStateListOf<Any>(ListScreen) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = backStack.lastOrNull() is ListScreen,
                                onClick = {
                                    if (backStack.lastOrNull() !is ListScreen)
                                        backStack.add(ListScreen)
                                },
                                icon = {
                                    Icon(Icons.Default.Home, contentDescription = "List")
                                },
                                label = { Text("List") }
                            )

                            NavigationBarItem(
                                selected = backStack.lastOrNull() is MapScreen,
                                onClick = {
                                    if (backStack.lastOrNull() !is MapScreen)
                                        backStack.add(MapScreen)
                                },
                                icon = {
                                    Icon(Icons.Default.Place, contentDescription = "Map")
                                },
                                label = { Text("Map") }
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
                                    onItemClick = {
                                        backStack.add(DetailScreen(it))
                                    }
                                )
                            }
                            entry<MapScreen> {
                                ScreenMap(
                                    onItemClick = {
                                        backStack.add(DetailScreen(it))
                                    }
                                )
                            }
                            entry<DetailScreen> {
                                val user = it.user
                                ScreenDetail(user)
                            }
                        }
                    )
                }
            }
        }
    }
}
