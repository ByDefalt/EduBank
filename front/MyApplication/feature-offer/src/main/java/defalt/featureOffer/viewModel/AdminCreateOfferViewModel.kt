package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.offer.OffersPostRequest
import defalt.featureOffer.usecase.CreateOfferUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminCreateOfferViewModel(
    private val createOffer: CreateOfferUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun create(title: String, description: String, state: OffersPostRequest.State, startDate: LocalDate, endDate: LocalDate) {
        val request = OffersPostRequest(title = title, description = description, state = state, startDate = startDate, endDate = endDate)
        launchWithUiState(_uiState) {
            createOffer(request)
        }
    }
}
