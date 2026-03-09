package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccount
import defalt.featureBank.usecase.AdminGetAllBankAccountsUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminBankListViewModel(
    private val getAllBankAccounts: AdminGetAllBankAccountsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BankAccount>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BankAccount>>> = _uiState.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAllBankAccounts()
    }
}
