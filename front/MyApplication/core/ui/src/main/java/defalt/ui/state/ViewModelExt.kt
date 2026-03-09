package defalt.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T, R> ViewModel.launchWithUiState(
    stateFlow: MutableStateFlow<UiState<R>>,
    transform: (T) -> R,
    block: suspend () -> NetworkResult<T>,
) {
    viewModelScope.launch {
        stateFlow.value = UiState.Loading
        stateFlow.value = block().toUiState(transform)
    }
}

fun <T> ViewModel.launchWithUiState(
    stateFlow: MutableStateFlow<UiState<Unit>>,
    block: suspend () -> NetworkResult<T>,
) = launchWithUiState(stateFlow, { }) { block() }
