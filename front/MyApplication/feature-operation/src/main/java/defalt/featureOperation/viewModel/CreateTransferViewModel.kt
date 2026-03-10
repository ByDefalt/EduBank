package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.CreateTransfer
import defalt.featureOperation.usecase.GetMyBankAccounts
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ── État du formulaire (données accumulées au fil des étapes) ─────────────────

data class CreateTransferForm(
    val sourceAccount: BankAccount? = null,
    val receiverId: String? = null,
    val receiverName: String? = null,
    val receiverIban: String? = null,
    val amount: String = "",
    val label: String = "",
)

// ── États des listes chargées ─────────────────────────────────────────────────

data class DebitStepData(
    val accounts: List<BankAccount>,
)

data class ReceiverStepData(
    val accounts: List<BankAccount>,
    val beneficiaries: List<Beneficiary>,
    val sourceAccountId: String? = null,
) {
    val filteredAccounts: List<BankAccount>
        get() = accounts.filter { it.id != sourceAccountId }
}

// ── ViewModel partagé pour tout le wizard de création de virement ─────────────

class CreateTransferViewModel(
    private val getMyBankAccounts: GetMyBankAccounts,
    private val getMyBeneficiaries: GetMyBeneficiaries,
    private val createTransfer: CreateTransfer,
) : ViewModel() {

    // Données chargées – étape 1 : compte à débiter
    private val _debitUiState = MutableStateFlow<UiState<DebitStepData>>(UiState.Loading)
    val debitUiState: StateFlow<UiState<DebitStepData>> = _debitUiState.asStateFlow()

    // Données chargées – étape 2 : destinataire
    private val _receiverUiState = MutableStateFlow<UiState<ReceiverStepData>>(UiState.Loading)
    val receiverUiState: StateFlow<UiState<ReceiverStepData>> = _receiverUiState.asStateFlow()

    // État de la soumission finale
    private val _submitUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitUiState: StateFlow<UiState<Unit>> = _submitUiState.asStateFlow()

    // Formulaire accumulé
    private val _form = MutableStateFlow(CreateTransferForm())
    val form: StateFlow<CreateTransferForm> = _form.asStateFlow()

    init {
        loadDebitAccounts()
        loadReceiverData()
    }

    // ── Étape 1 : compte à débiter ────────────────────────────────────────────

    fun retryDebit() = loadDebitAccounts()

    private fun loadDebitAccounts() = launchWithUiState(
        stateFlow = _debitUiState,
        transform = { DebitStepData(accounts = it) },
    ) {
        getMyBankAccounts()
    }

    fun selectSourceAccount(account: BankAccount) {
        _form.update { it.copy(sourceAccount = account) }
        loadReceiverData()
    }

    // ── Étape 2 : destinataire ────────────────────────────────────────────────

    fun retryReceiver() = loadReceiverData()

    private fun loadReceiverData() {
        launchWithUiState(
            stateFlow = _receiverUiState,
            transform = { it },
        ) {
            val accountsResult = getMyBankAccounts()
            if (accountsResult is defalt.utils.NetworkResult.Error) {
                return@launchWithUiState defalt.utils.NetworkResult.Error(accountsResult.code, accountsResult.message)
            }
            if (accountsResult is defalt.utils.NetworkResult.Exception) {
                return@launchWithUiState defalt.utils.NetworkResult.Exception(accountsResult.throwable)
            }
            accountsResult as defalt.utils.NetworkResult.Success

            val beneficiariesResult = getMyBeneficiaries()
            if (beneficiariesResult is defalt.utils.NetworkResult.Error) {
                return@launchWithUiState defalt.utils.NetworkResult.Error(beneficiariesResult.code, beneficiariesResult.message)
            }
            if (beneficiariesResult is defalt.utils.NetworkResult.Exception) {
                return@launchWithUiState defalt.utils.NetworkResult.Exception(beneficiariesResult.throwable)
            }
            beneficiariesResult as defalt.utils.NetworkResult.Success

            defalt.utils.NetworkResult.Success(
                ReceiverStepData(
                    accounts = accountsResult.data,
                    beneficiaries = beneficiariesResult.data,
                    sourceAccountId = _form.value.sourceAccount?.id,
                ),
            )
        }
    }

    /** Sélection d'un compte interne comme destinataire. */
    fun selectReceiverAccount(account: BankAccount) {
        _form.update {
            it.copy(
                receiverId = account.id,
                receiverName = accountTypeLabel(account.typeId),
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

    // ── Étape 3 : montant ─────────────────────────────────────────────────────

    fun setAmount(amount: String) {
        _form.update { it.copy(amount = amount) }
    }

    // ── Étape 4 : libellé ─────────────────────────────────────────────────────

    fun setLabel(label: String) {
        _form.update { it.copy(label = label) }
    }

    // ── Étape 5 : confirmation / soumission ───────────────────────────────────

    fun submitTransfer() = launchWithUiState(_submitUiState) {
        val f = _form.value
        createTransfer(
            ibanTarget = f.receiverIban!!,
            amount = f.amount.toDouble(),
            label = f.label,
        )
    }

    fun resetSubmitState() {
        _submitUiState.update { UiState.Idle }
    }

    // ── Réinitialisation du wizard ────────────────────────────────────────────

    fun reset() {
        _form.update { CreateTransferForm() }
        _submitUiState.update { UiState.Idle }
    }
}

// ── Données de prévisualisation ───────────────────────────────────────────────

internal fun sampleTransferAccounts(): List<BankAccount> = listOf(
    BankAccount(id = "1", parameterId = 0, typeId = 1, sold = 1_679_138.00, iban = "FR7630006000011234567890140"),
    BankAccount(id = "2", parameterId = 0, typeId = 2, sold = 775_854.79, iban = "FR7630006000013333333333340"),
    BankAccount(id = "3", parameterId = 0, typeId = 3, sold = 1_080_899.08, iban = "FR7630006000014444444444440"),
)

internal fun sampleBeneficiaries2(): List<Beneficiary> = listOf(
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1234 5678 9012", name = "Alice Dupont", id = 1),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 2222 3333 4444", name = "Amine Saïd", id = 2),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 5555 6666 7777", name = "Bruno Martin", id = 3),
)

internal fun accountTypeLabel(typeId: Int?): String = when (typeId) {
    1 -> "COMPTE CHÈQUES"
    2 -> "COMPTE ÉPARGNE"
    3 -> "COMPTE PROFESSIONNEL"
    else -> "COMPTE"
}
