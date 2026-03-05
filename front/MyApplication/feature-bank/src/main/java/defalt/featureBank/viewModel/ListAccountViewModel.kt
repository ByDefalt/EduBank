package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccount
import defalt.domain.repository.service.IBankRepository
import defalt.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListAccountViewModel(
) : ViewModel() {

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
        }
    }
}



