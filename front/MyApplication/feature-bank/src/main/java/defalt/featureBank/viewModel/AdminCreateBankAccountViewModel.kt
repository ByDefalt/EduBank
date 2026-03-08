package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.account.Account
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.State
import defalt.featureBank.usecase.AdminCreateBankAccountUseCase
import defalt.featureBank.usecase.AdminGetAllAccountsForBankUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminCreateBankAccountViewModel(
    private val createBankAccount: AdminCreateBankAccountUseCase,
    private val getAllAccounts: AdminGetAllAccountsForBankUseCase,
) : ViewModel() {

    private val _accountsState = MutableStateFlow<UiState<List<Account>>>(UiState.Loading)
    val accountsState: StateFlow<UiState<List<Account>>> = _accountsState.asStateFlow()

    private val _createState = MutableStateFlow<UiState<BankAccountDetail>>(UiState.Idle)
    val createState: StateFlow<UiState<BankAccountDetail>> = _createState.asStateFlow()

    init { loadAccounts() }

    fun retryAccounts() = loadAccounts()

    private fun loadAccounts() = launchWithUiState(stateFlow = _accountsState, transform = { it }) {
        getAllAccounts()
    }

    fun create(
        accountId: Int,
        iban: String,
        typeId: Int,
        sold: Double,
        overdraftLimit: Double,
        state: State,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            _createState.update { UiState.Loading }
            val request = BankAccountCreateRequest(
                typeId = typeId,
                iban = iban,
                sold = sold,
                overdraftLimit = overdraftLimit,
                state = state,
            )
            when (val r = createBankAccount(accountId, request)) {
                is NetworkResult.Success -> { _createState.update { UiState.Success(r.data) }; onSuccess() }
                is NetworkResult.Error -> _createState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _createState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}

