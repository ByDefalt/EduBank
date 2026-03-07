package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.offer.Offer
import defalt.featureOffer.usecase.GetAllOffersUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OffersViewModel(
    private val getAllOffers: GetAllOffersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Offer>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Offer>>> = _uiState.asStateFlow()

    init { loadOffers() }

    fun retry() = loadOffers()

    private fun loadOffers() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getAllOffers()
    }
}
