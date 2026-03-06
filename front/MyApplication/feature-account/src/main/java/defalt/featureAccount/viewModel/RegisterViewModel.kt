package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureAccount.usecase.RegisterClientAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(
    private val registerClientAccountUseCase: RegisterClientAccountUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun register(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        address: String,
        phoneNumber: String,
    ) = launchWithUiState(_uiState) {
        registerClientAccountUseCase(
            email = email,
            password = password,
            firstname = firstname,
            lastname = lastname,
            address = address,
            phoneNumber = phoneNumber,
        )
    }
}
