package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.bank.BankAccount
import defalt.domain.repository.service.IBankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ListAccountUiState {
    data object Loading : ListAccountUiState()
    data class Success(val accounts: List<BankAccount>) : ListAccountUiState()
    data class Error(val message: String) : ListAccountUiState()
}

class ListAccountViewModel(
    private val bankRepository: IBankRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListAccountUiState>(ListAccountUiState.Loading)
    val uiState: StateFlow<ListAccountUiState> = _uiState.asStateFlow()

    init {
        loadAccounts()
    }

    fun loadAccounts() {
        viewModelScope.launch {
            _uiState.value = ListAccountUiState.Loading
            // TODO : gérer le NetworkResult (success/error) une fois IBankRepository implémenté
            // val result = bankRepository.getMyBankAccounts()
            // _uiState.value = when (result) {
            //     is NetworkResult.Success -> ListAccountUiState.Success(result.data)
            //     is NetworkResult.Error   -> ListAccountUiState.Error(result.message)
            // }
        }
    }
}

