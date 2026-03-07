package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BeneficiariesViewModel(
    private val getMyBeneficiaries: GetMyBeneficiaries,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Beneficiary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Beneficiary>>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    fun onQueryChange(q: String) {
        _query.update { q }
    }

    private fun load() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getMyBeneficiaries()
    }
}
