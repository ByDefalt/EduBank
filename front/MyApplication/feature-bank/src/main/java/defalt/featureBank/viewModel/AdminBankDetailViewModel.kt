package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.featureBank.usecase.AdminDeleteBankAccountUseCase
import defalt.featureBank.usecase.AdminGetBankAccountByIdUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountParamUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminBankDetailViewModel(
    private val getBankAccountById: AdminGetBankAccountByIdUseCase,
    private val deleteBankAccount: AdminDeleteBankAccountUseCase,
    private val updateBankAccountParam: AdminUpdateBankAccountParamUseCase,
    private val updateBankAccount: AdminUpdateBankAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BankAccountDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<BankAccountDetail>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    fun load(id: String) = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getBankAccountById(id)
    }

    // Mise à jour complète : type + état + découvert
    fun updateFull(id: String, typeId: Int, overdraftLimit: Double, state: State) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            val param = BankAccountParameter(overdraftLimit = overdraftLimit, state = state)
            when (val r = updateBankAccount(id, typeId, param)) {
                is NetworkResult.Success -> {
                    _actionState.update { UiState.Success(Unit) }
                    _uiState.update { UiState.Success(r.data) }
                }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    // Mise à jour découvert uniquement (UC15 — conservé pour compatibilité)
    fun update(id: String, overdraftLimit: Double) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            val param = BankAccountParameter(overdraftLimit = overdraftLimit)
            when (val r = updateBankAccountParam(id, param)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    fun delete(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = deleteBankAccount(id)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; onSuccess() }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}
