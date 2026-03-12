package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.operation.Operation
import defalt.featureOperation.usecase.GetAllOperationsUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class AdminOperationListViewModel(
    private val getAllOperations: GetAllOperationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Operation>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Operation>>> = _uiState.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAllOperations()
    }
}
