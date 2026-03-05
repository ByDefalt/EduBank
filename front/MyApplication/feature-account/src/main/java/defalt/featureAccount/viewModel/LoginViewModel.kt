package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    init {
        // Pas de chargement initial pour un formulaire de login
    }

    fun retry() = login("", "")

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case de login
            delay(2000)
            _uiState.update { UiState.Success(Unit) }
        }
    }
}

