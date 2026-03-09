package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureOperation.usecase.AddBeneficiary
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddBeneficiaryViewModel(
    private val addBeneficiary: AddBeneficiary,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun add(name: String, iban: String) = launchWithUiState(_uiState) {
        addBeneficiary(name = name, iban = iban)
    }
}
