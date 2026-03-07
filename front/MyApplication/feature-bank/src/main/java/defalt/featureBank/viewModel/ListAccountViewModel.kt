
package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccountDetail
import defalt.featureBank.usecase.GetAllMyAccount
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ListAccountViewModel(
    private val getAllMyAccount: GetAllMyAccount,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BankAccountDetail>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BankAccountDetail>>> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    // Intention UI exposée : appelée depuis l'écran en cas d'erreur
    fun retry() = loadAccounts()

    private fun loadAccounts() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAllMyAccount()
    }
}
