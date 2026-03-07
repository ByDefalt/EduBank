package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.account.RoleEnum
import defalt.featureAccount.usecase.SignInClientAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val signInClientAccountUseCase: SignInClientAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<RoleEnum>>(UiState.Idle)
    val uiState: StateFlow<UiState<RoleEnum>> = _uiState.asStateFlow()


    fun login(identifier: String, password: String) =
        launchWithUiState(_uiState, transform = { it }) {
            signInClientAccountUseCase(identifier, password)
        }
}
