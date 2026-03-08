package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.featureOffer.usecase.DeleteOfferUseCase
import defalt.featureOffer.usecase.GetOfferByIdUseCase
import defalt.featureOffer.usecase.UpdateOfferUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminOfferDetailViewModel(
    private val getOfferById: GetOfferByIdUseCase,
    private val updateOffer: UpdateOfferUseCase,
    private val deleteOffer: DeleteOfferUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Offer>>(UiState.Loading)
    val uiState: StateFlow<UiState<Offer>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    fun load(id: Int) = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getOfferById(id)
    }

    fun save(id: Int, title: String, description: String, state: OffersIdPutRequest.State, startDate: LocalDate, endDate: LocalDate) {
        val request = OffersIdPutRequest(title = title, description = description, state = state, startDate = startDate, endDate = endDate)
        launchWithUiState(_actionState) {
            updateOffer(id, request).also { if (it is NetworkResult.Success) load(id) }
        }
    }

    fun delete(id: Int, onSuccess: () -> Unit) = launchWithUiState(_actionState) {
        deleteOffer(id).also { if (it is NetworkResult.Success) onSuccess() }
    }
}
