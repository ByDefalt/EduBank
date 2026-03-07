package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccountDetail
import defalt.featureBank.usecase.GetHomeAccount
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeAccountViewModel(
    private val getHomeAccount: GetHomeAccount,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BankAccountDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<BankAccountDetail>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getHomeAccount()
    }
}
