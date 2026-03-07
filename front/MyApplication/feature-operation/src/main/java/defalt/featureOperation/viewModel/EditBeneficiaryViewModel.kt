package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.DeleteBeneficiary
import defalt.featureOperation.usecase.EditBeneficiary
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditBeneficiaryViewModel(
    private val getMyBeneficiaries: GetMyBeneficiaries,
    private val editBeneficiary: EditBeneficiary,
    private val deleteBeneficiary: DeleteBeneficiary,
) : ViewModel() {

    private val _beneficiary = MutableStateFlow<UiState<Beneficiary>>(UiState.Loading)
    val beneficiary: StateFlow<UiState<Beneficiary>> = _beneficiary.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    fun load(id: Int) = launchWithUiState(stateFlow = _beneficiary, transform = { beneficiary ->
        beneficiary.first { it.id == id }
    }) {
        getMyBeneficiaries()
    }

    fun save(id: Int, name: String, iban: String, accountSourceId: String) =
        launchWithUiState(_actionState) {
            editBeneficiary(id = id, name = name, iban = iban, accountSourceId = accountSourceId)
        }

    fun delete(id: Int) = launchWithUiState(_actionState) {
        deleteBeneficiary(id = id)
    }
}
