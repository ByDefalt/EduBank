package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.featureAccount.usecase.SignInClientAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInClientAccountUseCase: SignInClientAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Success(Unit))
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    init {
        // Pas de chargement initial pour un formulaire de login
    }

    fun retry() = login("", "")

    fun login(identifier: String, password: String) =
        launchWithUiState(_uiState) {
            signInClientAccountUseCase(identifier, password)
        }
}
