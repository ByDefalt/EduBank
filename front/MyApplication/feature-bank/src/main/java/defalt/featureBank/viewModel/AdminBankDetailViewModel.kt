package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.Type as BankAccountType
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type
import defalt.featureBank.usecase.AdminDeleteBankAccountUseCase
import defalt.featureBank.usecase.AdminGetBankAccountByIdUseCase
import defalt.featureBank.usecase.AdminGetBankAccountTypesUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountParamUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import defalt.utils.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminBankDetailViewModel(
    private val getBankAccountById: AdminGetBankAccountByIdUseCase,
    private val deleteBankAccount: AdminDeleteBankAccountUseCase,
    private val updateBankAccountParam: AdminUpdateBankAccountParamUseCase,
    private val updateBankAccount: AdminUpdateBankAccountUseCase,
    private val getBankAccountTypes: AdminGetBankAccountTypesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<BankAccountDetail>>(UiState.Loading)
    val uiState: StateFlow<UiState<BankAccountDetail>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    // Liste des types chargés depuis l'API
    private val _typesState = MutableStateFlow<UiState<List<Type>>>(UiState.Loading)
    val typesState: StateFlow<UiState<List<Type>>> = _typesState.asStateFlow()

    fun load(id: String) {
        launchWithUiState(stateFlow = _uiState, transform = { it }) {
            getBankAccountById(id)
        }
        launchWithUiState(stateFlow = _typesState, transform = { it }) {
            getBankAccountTypes()
        }
    }

    /**
     * Met à jour le type ET les paramètres (découvert + état) en une seule action.
     * Utilise _actionState pour ne pas écraser les données affichées dans _uiState.
     * Une fois réussi, recharge le détail pour refléter les nouvelles valeurs.
     */
    fun updateFull(id: String, typeId: Int, overdraftLimit: Double, state: State) {
        val param = BankAccountParameter(overdraftLimit = overdraftLimit, state = state)
        launchWithUiState(stateFlow = _actionState) {
            val result = updateBankAccount(id, typeId, param)
            if (result is NetworkResult.Success) {
                // Recharger le détail pour mettre à jour l'affichage
                launchWithUiState(stateFlow = _uiState, transform = { it }) {
                    getBankAccountById(id)
                }
            }
            result.map { it }
        }
    }

    fun delete(id: String, onSuccess: () -> Unit) = launchWithUiState(_actionState) {
        deleteBankAccount(id).also { if (it is NetworkResult.Success) onSuccess() }
    }
}