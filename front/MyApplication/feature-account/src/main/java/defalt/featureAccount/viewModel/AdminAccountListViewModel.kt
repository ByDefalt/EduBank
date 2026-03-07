package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.account.Account
import defalt.featureAccount.usecase.GetAllAccountsUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminAccountListViewModel(
    private val getAllAccounts: GetAllAccountsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Account>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Account>>> = _uiState.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAllAccounts()
    }
}
