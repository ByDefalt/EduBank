package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.operation.Operation
import defalt.featureBank.usecase.GetAccountDetailsAndOperation
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AccountDetailsData(
    val account: BankAccountDetail,
    val operations: List<Operation>,
)

class AccountDetailsViewModel(
    private val getAccountDetailsAndOperation: GetAccountDetailsAndOperation,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AccountDetailsData>>(UiState.Loading)
    val uiState: StateFlow<UiState<AccountDetailsData>> = _uiState.asStateFlow()

    private var currentAccountId: String = ""

    fun load(accountId: String) {
        currentAccountId = accountId
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() = launchWithUiState(
        stateFlow = _uiState,
        transform = { result ->
            AccountDetailsData(
                account = result.accountDetail,
                operations = result.operations,
            )
        },
    ) {
        getAccountDetailsAndOperation(currentAccountId)
    }
}
