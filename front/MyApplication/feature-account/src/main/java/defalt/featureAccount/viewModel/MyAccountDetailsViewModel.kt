package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.usecase.GetAccountByIdUseCase
import defalt.domain.session.Session
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyAccountDetailsViewModel(
    private val getAccountById: GetAccountByIdUseCase,
    private val session: Session,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AccountWithInfo>>(UiState.Loading)
    val uiState: StateFlow<UiState<AccountWithInfo>> = _uiState.asStateFlow()

    fun load() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        val id = session.accountId!!
        getAccountById(id)
    }
}

