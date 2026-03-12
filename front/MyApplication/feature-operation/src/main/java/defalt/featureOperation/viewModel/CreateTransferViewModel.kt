package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.Type
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.CreateTransfer
import defalt.featureOperation.usecase.GetAllMyAccount
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update



data class CreateTransferForm(
    val sourceAccount: BankAccountDetail? = null,
    val receiverId: String? = null,
    val receiverName: String? = null,
    val receiverIban: String? = null,
    val amount: String = "",
    val label: String = "",
)



data class DebitStepData(
    val accounts: List<BankAccountDetail>,
)

data class ReceiverStepData(
    val accounts: List<BankAccountDetail>,
    val beneficiaries: List<Beneficiary>,
    val sourceAccountId: String? = null,
) {
    val filteredAccounts: List<BankAccountDetail>
        get() = accounts.filter { it.id != sourceAccountId }
}



class CreateTransferViewModel(
    private val getAllMyAccount: GetAllMyAccount,
    private val getMyBeneficiaries: GetMyBeneficiaries,
    private val createTransfer: CreateTransfer,
) : ViewModel() {


    private val _debitUiState = MutableStateFlow<UiState<DebitStepData>>(UiState.Loading)
    val debitUiState: StateFlow<UiState<DebitStepData>> = _debitUiState.asStateFlow()


    private val _receiverUiState = MutableStateFlow<UiState<ReceiverStepData>>(UiState.Loading)
    val receiverUiState: StateFlow<UiState<ReceiverStepData>> = _receiverUiState.asStateFlow()


    private val _submitUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitUiState: StateFlow<UiState<Unit>> = _submitUiState.asStateFlow()


    private val _form = MutableStateFlow(CreateTransferForm())
    val form: StateFlow<CreateTransferForm> = _form.asStateFlow()

    init {
        loadDebitAccounts()
        loadReceiverData()
    }



    fun retryDebit() = loadDebitAccounts()

    private fun loadDebitAccounts() = launchWithUiState(
        stateFlow = _debitUiState,
        transform = { DebitStepData(accounts = it) },
    ) {
        getAllMyAccount()
    }

    fun selectSourceAccount(account: BankAccountDetail) {
        _form.update { it.copy(sourceAccount = account) }
        loadReceiverData()
    }



    fun retryReceiver() = loadReceiverData()

    private fun loadReceiverData() {
        launchWithUiState(
            stateFlow = _receiverUiState,
            transform = { it },
        ) {
            val accountsResult = getAllMyAccount()
            if (accountsResult is NetworkResult.Error) {
                return@launchWithUiState NetworkResult.Error(accountsResult.code, accountsResult.message)
            }
            if (accountsResult is NetworkResult.Exception) {
                return@launchWithUiState NetworkResult.Exception(accountsResult.throwable)
            }
            accountsResult as NetworkResult.Success

            val beneficiariesResult = getMyBeneficiaries()
            if (beneficiariesResult is NetworkResult.Error) {
                return@launchWithUiState NetworkResult.Error(beneficiariesResult.code, beneficiariesResult.message)
            }
            if (beneficiariesResult is NetworkResult.Exception) {
                return@launchWithUiState NetworkResult.Exception(beneficiariesResult.throwable)
            }
            beneficiariesResult as NetworkResult.Success

            NetworkResult.Success(
                ReceiverStepData(
                    accounts = accountsResult.data,
                    beneficiaries = beneficiariesResult.data,
                    sourceAccountId = _form.value.sourceAccount?.id,
                ),
            )
        }
    }

    /** Sélection d'un compte interne comme destinataire. */
    fun selectReceiverAccount(account: BankAccountDetail) {
        _form.update {
            it.copy(
                receiverId = account.id,
                receiverName = account.type?.name ?: "COMPTE",
                receiverIban = account.iban,
            )
        }
    }

    /** Sélection d'un bénéficiaire comme destinataire. */
    fun selectReceiverBeneficiary(beneficiary: Beneficiary) {
        _form.update {
            it.copy(
                receiverId = beneficiary.id?.toString(),
                receiverName = beneficiary.name,
                receiverIban = beneficiary.ibanTarget,
            )
        }
    }



    fun setAmount(amount: String) {
        _form.update { it.copy(amount = amount) }
    }



    fun setLabel(label: String) {
        _form.update { it.copy(label = label) }
    }



    fun submitTransfer() = launchWithUiState(_submitUiState) {
        val f = _form.value
        createTransfer(
            ibanTarget = f.receiverIban!!,
            amount = f.amount.toDouble(),
            label = f.label,
            accountSourceId = f.sourceAccount!!.id!!,
        )
    }

    fun resetSubmitState() {
        _submitUiState.update { UiState.Idle }
    }



    fun reset() {
        _form.update { CreateTransferForm() }
        _submitUiState.update { UiState.Idle }
    }
}



internal fun sampleTransferAccounts(): List<BankAccountDetail> = listOf(
    BankAccountDetail(id = "1", parameter = null, type = Type(id = 1, name = "COMPTE CHÈQUES"), sold = 1_679_138.00, iban = "FR7630006000011234567890140"),
    BankAccountDetail(id = "2", parameter = null, type = Type(id = 2, name = "COMPTE ÉPARGNE"), sold = 775_854.79, iban = "FR7630006000013333333333340"),
    BankAccountDetail(id = "3", parameter = null, type = Type(id = 3, name = "COMPTE PROFESSIONNEL"), sold = 1_080_899.08, iban = "FR7630006000014444444444440"),
)

internal fun sampleBeneficiaries2(): List<Beneficiary> = listOf(
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1234 5678 9012", name = "Alice Dupont", id = 1),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 2222 3333 4444", name = "Amine Saïd", id = 2),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 5555 6666 7777", name = "Bruno Martin", id = 3),
)


