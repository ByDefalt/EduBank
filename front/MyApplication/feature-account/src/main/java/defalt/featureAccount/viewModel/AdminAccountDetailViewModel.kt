package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.account.PersonalInformation
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.usecase.ActivateAccountUseCase
import defalt.featureAccount.usecase.DeactivateAccountUseCase
import defalt.featureAccount.usecase.GetAccountByIdUseCase
import defalt.featureAccount.usecase.UpdatePersonalInfoUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminAccountDetailViewModel(
    private val getAccountById: GetAccountByIdUseCase,
    private val activateAccount: ActivateAccountUseCase,
    private val deactivateAccount: DeactivateAccountUseCase,
    private val updatePersonalInfo: UpdatePersonalInfoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AccountWithInfo>>(UiState.Loading)
    val uiState: StateFlow<UiState<AccountWithInfo>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    fun load(id: String) = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAccountById(id)
    }

    fun activate(id: String) = launchWithUiState(_actionState) {
        activateAccount(id).also { if (it is defalt.utils.NetworkResult.Success) load(id) }
    }

    fun deactivate(id: String) = launchWithUiState(_actionState) {
        deactivateAccount(id).also { if (it is defalt.utils.NetworkResult.Success) load(id) }
    }

    fun updateInfo(id: String, info: PersonalInformation) {
        val infoId = (_uiState.value as? UiState.Success)?.data?.personalInfo?.id ?: return
        launchWithUiState(_actionState) {
            updatePersonalInfo(infoId, info).also { if (it is defalt.utils.NetworkResult.Success) load(id) }
        }
    }
}
