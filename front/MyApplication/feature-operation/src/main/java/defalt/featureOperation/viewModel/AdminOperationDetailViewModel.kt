package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.usecase.CancelOperationUseCase
import defalt.featureOperation.usecase.GetOperationByIdUseCase
import defalt.featureOperation.usecase.UpdateOperationStateUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ── UC8/UC13/UC20/UC21 : Détail + actions opération ─────────────────────────
class AdminOperationDetailViewModel(
    private val getOperationById: GetOperationByIdUseCase,
    private val cancelOperation: CancelOperationUseCase,
    private val updateOperationState: UpdateOperationStateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Operation>>(UiState.Loading)
    val uiState: StateFlow<UiState<Operation>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    fun load(id: Int) = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getOperationById(id)
    }

    // UC20 : Annuler l'opération
    fun cancel(id: Int) = launchWithUiState(_actionState) {
        cancelOperation(id).also { if (it is NetworkResult.Success) load(id) }
    }

    // UC13/UC21 : Changer / mettre à jour l'état
    fun updateState(id: Int, state: OperationState) = launchWithUiState(_actionState) {
        updateOperationState(id, state).also { if (it is NetworkResult.Success) load(id) }
    }
}
