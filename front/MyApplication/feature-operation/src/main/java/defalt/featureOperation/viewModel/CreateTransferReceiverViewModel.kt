package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Beneficiary
import defalt.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReceiverData(
    val accounts: List<BankAccount>,
    val beneficiaries: List<Beneficiary>,
)

class CreateTransferReceiverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ReceiverData>>(UiState.Loading)
    val uiState: StateFlow<UiState<ReceiverData>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler les use cases pour charger comptes et bénéficiaires
            delay(2000)
            _uiState.update {
                UiState.Success(
                    ReceiverData(
                        accounts = sampleAccounts(),
                        beneficiaries = sampleBeneficiaries(),
                    ),
                )
            }
        }
    }
}

private fun sampleAccounts(): List<BankAccount> = listOf(
    BankAccount(id = "1", parameterId = 0, typeId = 1, sold = 1679138.00, iban = "FR7630006000011234567890140"),
    BankAccount(id = "2", parameterId = 0, typeId = 2, sold = 775854.79, iban = "FR7630006000013333333333340"),
    BankAccount(id = "3", parameterId = 0, typeId = 3, sold = 1080899.08, iban = "FR7630006000014444444444440"),
)

private fun sampleBeneficiaries(): List<Beneficiary> = listOf(
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1234 5678 9012", name = "Alice Dupont", id = 1),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 2222 3333 4444", name = "Amine Saïd", id = 2),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 5555 6666 7777", name = "Bruno Martin", id = 3),
)

