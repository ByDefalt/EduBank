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

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    init {
        // Pas de chargement initial pour un formulaire d'inscription
    }

    fun retry() = register("", "")

    fun register(identifier: String, password: String) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case d'inscription
            delay(2000)
            _uiState.update { UiState.Success(Unit) }
        }
    }
}
