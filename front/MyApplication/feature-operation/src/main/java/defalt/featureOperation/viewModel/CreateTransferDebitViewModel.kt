package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccount
import defalt.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTransferDebitViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BankAccount>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BankAccount>>> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    fun retry() = loadAccounts()

    private fun loadAccounts() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case pour charger les comptes à débiter
            delay(2000)
            _uiState.update { UiState.Success(sampleAccounts()) }
        }
    }
}

private fun sampleAccounts(): List<BankAccount> = listOf(
    BankAccount(id = "1", parameterId = 0, typeId = 1, sold = 1679138.00, iban = "FR7630006000011234567890140"),
    BankAccount(id = "2", parameterId = 0, typeId = 2, sold = 775854.79, iban = "FR7630006000013333333333340"),
    BankAccount(id = "3", parameterId = 0, typeId = 3, sold = 1080899.08, iban = "FR7630006000014444444444440"),
)
