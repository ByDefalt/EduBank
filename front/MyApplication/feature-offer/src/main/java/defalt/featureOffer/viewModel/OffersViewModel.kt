package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.offer.Offer
import defalt.ui.state.UiState
import java.time.LocalDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OffersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Offer>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Offer>>> = _uiState.asStateFlow()

    init {
        loadOffers()
    }

    fun retry() = loadOffers()

    private fun loadOffers() {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            // TODO : appeler le use case pour charger les offres
            delay(2000)
            _uiState.update { UiState.Success(sampleOffers()) }
        }
    }
}

private fun sampleOffers(): List<Offer> = listOf(
    Offer(
        id = 1,
        title = "Offre de bienvenue",
        description = "Taux préférentiel pour les nouveaux clients",
        state = Offer.State.ACTIVE,
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusMonths(1),
        picturePath = null,
    ),
    Offer(
        id = 2,
        title = "Crédit pro",
        description = "Financement à taux avantageux pour les entreprises",
        state = Offer.State.ACTIVE,
        startDate = LocalDate.now().minusMonths(2),
        endDate = LocalDate.now().plusMonths(2),
        picturePath = null,
    ),
)
