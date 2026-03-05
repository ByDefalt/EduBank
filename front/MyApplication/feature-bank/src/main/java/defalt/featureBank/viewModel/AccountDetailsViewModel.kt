package defalt.featureBank.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.ui.state.UiState
import java.time.OffsetDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountDetailsData(
    val account: BankAccount,
    val operations: List<Operation>,
)

class AccountDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val accountId: Int = savedStateHandle["accountId"] ?: 0

    private val _uiState = MutableStateFlow<UiState<AccountDetailsData>>(UiState.Loading)
    val uiState: StateFlow<UiState<AccountDetailsData>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler les use cases pour charger le compte et ses opérations
            delay(2000)
            _uiState.update {
                UiState.Success(
                    AccountDetailsData(
                        account = sampleAccount(),
                        operations = sampleOperations(),
                    ),
                )
            }
        }
    }
}

private fun sampleAccount() = BankAccount(
    id = "1",
    parameterId = 0,
    typeId = 1,
    sold = 478.27,
    iban = "FR7630006000011234567890140",
)

private fun sampleOperations(): List<Operation> {
    val now = OffsetDateTime.now()
    return listOf(
        Operation(1, "1", "Virement reçu - Salaire", OperationState.COMPLETED, "FR76300060000198", 2350.00, now.minusDays(0).withHour(9).withMinute(0)),
        Operation(2, "1", "Paiement en ligne", OperationState.COMPLETED, "FR76300060000155", -49.99, now.minusDays(0).withHour(14).withMinute(30)),
        Operation(3, "1", "Virement vers épargne", OperationState.COMPLETED, "FR76300060000133", -500.00, now.minusDays(1).withHour(11).withMinute(15)),
        Operation(4, "1", "Remboursement ami", OperationState.COMPLETED, "FR76300060000144", 120.00, now.minusDays(1).withHour(18).withMinute(45)),
        Operation(5, "1", "Abonnement streaming", OperationState.COMPLETED, "FR76300060000111", -14.99, now.minusDays(3).withHour(8).withMinute(0)),
        Operation(6, "1", "Courses alimentaires", OperationState.COMPLETED, "FR76300060000122", -87.50, now.minusDays(3).withHour(17).withMinute(20)),
        Operation(7, "1", "Loyer", OperationState.COMPLETED, "FR76300060000166", -900.00, now.minusDays(5).withHour(7).withMinute(0)),
        Operation(8, "1", "Prélèvement assurance", OperationState.PENDING, "FR76300060000177", -32.50, now.plusDays(2).withHour(10).withMinute(0)),
        Operation(9, "1", "Virement programmé", OperationState.PENDING, "FR76300060000188", -250.00, now.plusDays(2).withHour(12).withMinute(0)),
        Operation(10, "1", "Remboursement prévu", OperationState.PENDING, "FR76300060000199", 75.00, now.plusDays(5).withHour(9).withMinute(0)),
    )
}

