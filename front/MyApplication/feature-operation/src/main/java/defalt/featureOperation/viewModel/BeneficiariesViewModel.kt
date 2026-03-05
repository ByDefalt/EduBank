package defalt.featureOperation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.operation.Beneficiary
import defalt.ui.state.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BeneficiariesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Beneficiary>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Beneficiary>>> = _uiState.asStateFlow()

    init {
        loadBeneficiaries()
    }

    fun retry() = loadBeneficiaries()

    private fun loadBeneficiaries() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case pour charger les bénéficiaires
            delay(2000)
            _uiState.update { UiState.Success(sampleBeneficiaries()) }
        }
    }
}

private fun sampleBeneficiaries(): List<Beneficiary> = listOf(
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1234 5678 9012", name = "Alice Dupont", id = 1),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 2222 3333 4444", name = "Amine Saïd", id = 2),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 5555 6666 7777", name = "Bruno Martin", id = 3),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 8888 9999 0000", name = "Claire Noël", id = 4),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1111 2222 3333", name = "David Petit", id = 5),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 4444 5555 6666", name = "Élodie Faure", id = 6),
)
