package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureAccount.usecase.LogoutUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow

class AdminHomeViewModel(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: MutableStateFlow<UiState<Unit>> = _uiState


    fun logout() = launchWithUiState(_uiState){
        logoutUseCase()
    }
}