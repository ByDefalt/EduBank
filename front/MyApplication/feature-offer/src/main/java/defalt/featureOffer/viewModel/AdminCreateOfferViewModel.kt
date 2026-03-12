package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.offer.OfferInput
import defalt.featureOffer.usecase.CreateOfferUseCase
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import defalt.utils.NetworkResult
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminCreateOfferViewModel(
    private val createOffer: CreateOfferUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    var onMutationSuccess: (() -> Unit)? = null

    fun create(title: String, description: String, state: OfferInput.State, startDate: LocalDate, endDate: LocalDate) {
        val request = OfferInput(title = title, description = description, state = state, startDate = startDate, endDate = endDate)
        launchWithUiState(_uiState) {
            createOffer(request).also {
                if (it is NetworkResult.Success) onMutationSuccess?.invoke()
            }
        }
    }
}
