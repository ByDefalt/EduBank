package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.account.Account
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type
import defalt.featureBank.usecase.AdminCreateBankAccountUseCase
import defalt.featureBank.usecase.AdminGetAllAccountsForBankUseCase
import defalt.featureBank.usecase.AdminGetBankAccountTypesUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminCreateBankAccountViewModel(
    private val createBankAccount: AdminCreateBankAccountUseCase,
    private val getAllAccounts: AdminGetAllAccountsForBankUseCase,
    private val getBankAccountTypes: AdminGetBankAccountTypesUseCase,
) : ViewModel() {

    private val _accountsState = MutableStateFlow<UiState<List<Account>>>(UiState.Loading)
    val accountsState: StateFlow<UiState<List<Account>>> = _accountsState.asStateFlow()

    private val _createState = MutableStateFlow<UiState<BankAccountDetail>>(UiState.Idle)
    val createState: StateFlow<UiState<BankAccountDetail>> = _createState.asStateFlow()

    private val _type = MutableStateFlow<UiState<List<Type>>>(UiState.Loading)
    val type: StateFlow<UiState<List<Type>>> = _type.asStateFlow()

    var onMutationSuccess: (() -> Unit)? = null

    init {
        loadAccounts()
        loadType()
    }

    fun retryAccounts() = loadAccounts()
    fun retryTypes() = loadType()

    private fun loadAccounts() = launchWithUiState(stateFlow = _accountsState, transform = { it }) {
        getAllAccounts()
    }

    private fun loadType() = launchWithUiState(stateFlow = _type, transform = { it }) {
        getBankAccountTypes()
    }

    fun create(
        accountId: String,
        iban: String,
        typeId: Int,
        sold: Double,
        overdraftLimit: Double,
        state: State,
        onSuccess: () -> Unit,
    ) {
        val request = BankAccountCreateRequest(
            typeId = typeId,
            iban = iban,
            sold = sold,
            overdraftLimit = overdraftLimit,
            state = state,
        )
        launchWithUiState(stateFlow = _createState, transform = { it }) {
            createBankAccount(accountId, request).also {
                if (it is NetworkResult.Success) {
                    onMutationSuccess?.invoke()
                    onSuccess()
                }
            }
        }
    }
}
