package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureAccount.usecase.SignInClientAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val signInClientAccountUseCase: SignInClientAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun retry() = login("", "")

    fun login(identifier: String, password: String) =
        launchWithUiState(_uiState) {
            signInClientAccountUseCase(identifier, password)
        }
}
