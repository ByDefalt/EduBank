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

class ListAccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<BankAccount>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<BankAccount>>> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    // Intention UI exposée : appelée depuis l'écran en cas d'erreur
    fun retry() = loadAccounts()

    private fun loadAccounts() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : gérer le NetworkResult (success/error) une fois IBankRepository implémenté
            // when (val result = bankRepository.getMyBankAccounts()) {
            //     is NetworkResult.Success -> _uiState.update { UiState.Success(result.data) }
            //     is NetworkResult.Error   -> _uiState.update { UiState.Error(result.message) }
            // }
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
        sold = 1679138.00,
        iban = "FR7630006000011234567890140",
    ),
    BankAccount(
        id = "2",
        parameterId = 0,
        typeId = 1,
        sold = 459393.44,
        iban = "FR7630006000019876543210140",
    ),
    BankAccount(
        id = "3",
        parameterId = 0,
        typeId = 1,
        sold = 5866841.38,
        iban = "FR7630006000015555555555540",
    ),
    BankAccount(
        id = "4",
        parameterId = 0,
        typeId = 2,
        sold = 775854.79,
        iban = "FR7630006000013333333333340",
    ),
    BankAccount(
        id = "5",
        parameterId = 0,
        typeId = 3,
        sold = 1080899.08,
        iban = "FR7630006000014444444444440",
    ),
)
