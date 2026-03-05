package defalt.featureBank.viewModel

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

class HomeAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BankAccount>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BankAccount>>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case pour charger les comptes
            delay(2000)
            _uiState.update { UiState.Success(sampleAccounts()) }
        }
    }
}

private fun sampleAccounts(): List<BankAccount> = listOf(
    BankAccount(
        id = "1",
        parameterId = 0,
        typeId = 1,
        sold = 478.27,
        iban = "FR7630006000011234567890140",
    ),
)
