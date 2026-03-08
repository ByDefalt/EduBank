package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.usecase.CancelOperationUseCase
import defalt.featureOperation.usecase.GetAllOperationsUseCase
import defalt.featureOperation.usecase.GetOperationByIdUseCase
import defalt.featureOperation.usecase.UpdateOperationStateUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── UC5 : Liste des opérations ───────────────────────────────────────────────
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

    // UC20 : Créer une opération d'annulation
    fun cancel(id: Int) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = cancelOperation(id)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    // UC13/UC21 : Changer / Mettre à jour l'état
    fun updateState(id: Int, state: OperationState) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = updateOperationState(id, state.value)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}
