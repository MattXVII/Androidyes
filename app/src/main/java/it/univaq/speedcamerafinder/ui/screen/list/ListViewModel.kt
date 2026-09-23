package it.univaq.speedcamerafinder.ui.screen.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.univaq.speedcamerafinder.common.Result
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.usecase.GetSpeedCamerasUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListUiState (
    val items: List<SpeedCamera> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getSpeedCamerasUseCase: GetSpeedCamerasUseCase
): ViewModel() {

    var uiState by mutableStateOf(ListUiState())
        private set

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            getSpeedCamerasUseCase().collect {
                uiState = when(it) {
                    is Result.Loading -> uiState.copy(isLoading = true)
                    is Result.Success -> uiState.copy(items = it.data, isLoading = false)
                    is Result.Error -> uiState.copy(error = it.message, isLoading = false)
                }
            }
        }
    }
}