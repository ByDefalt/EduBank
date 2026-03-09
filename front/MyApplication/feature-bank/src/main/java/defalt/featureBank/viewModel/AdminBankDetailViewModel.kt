package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
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

    fun updateFull(id: String, typeId: Int, overdraftLimit: Double, state: State) {
        val param = BankAccountParameter(overdraftLimit = overdraftLimit, state = state)
        launchWithUiState(stateFlow = _uiState, transform = { it }) {
            updateBankAccount(id, typeId, param).also {
                if (it is NetworkResult.Success) _actionState.value = UiState.Success(Unit)
            }
        }
    }

    fun update(id: String, overdraftLimit: Double) = launchWithUiState(_actionState) {
        updateBankAccountParam(id, BankAccountParameter(overdraftLimit = overdraftLimit))
            .also { if (it is NetworkResult.Success) load(id) }
    }

    fun delete(id: String, onSuccess: () -> Unit) = launchWithUiState(_actionState) {
        deleteBankAccount(id).also { if (it is NetworkResult.Success) onSuccess() }
    }
}
