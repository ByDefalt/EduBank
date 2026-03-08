package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.account.PersonalInformation
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.usecase.ActivateAccountUseCase
import defalt.featureAccount.usecase.DeactivateAccountUseCase
import defalt.featureAccount.usecase.GetAccountByIdUseCase
import defalt.featureAccount.usecase.UpdatePersonalInfoUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun activate(id: String) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = activateAccount(id)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    fun deactivate(id: String) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = deactivateAccount(id)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    fun updateInfo(id: String, info: PersonalInformation) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            val infoId = (_uiState.value as? UiState.Success)?.data?.personalInfo?.id ?: return@launch
            when (val r = updatePersonalInfo(infoId, info)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}
