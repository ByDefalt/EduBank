package defalt.featureOffer.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.domain.entity.offer.OffersPostRequest
import defalt.featureOffer.usecase.CreateOfferUseCase
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ── Liste admin ──────────────────────────────────────────────────────────────

// (OffersViewModel est réutilisé pour la liste admin)

// ── Détail / édition ─────────────────────────────────────────────────────────

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
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            val request = OffersIdPutRequest(title = title, description = description, state = state, startDate = startDate, endDate = endDate)
            when (val r = updateOffer(id, request)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; load(id) }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }

    fun delete(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _actionState.update { UiState.Loading }
            when (val r = deleteOffer(id)) {
                is NetworkResult.Success -> { _actionState.update { UiState.Success(Unit) }; onSuccess() }
                is NetworkResult.Error -> _actionState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _actionState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}

// ── Création ─────────────────────────────────────────────────────────────────

class AdminCreateOfferViewModel(
    private val createOffer: CreateOfferUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun create(title: String, description: String, state: OffersPostRequest.State, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }
            val request = OffersPostRequest(title = title, description = description, state = state, startDate = startDate, endDate = endDate)
            when (val r = createOffer(request)) {
                is NetworkResult.Success -> _uiState.update { UiState.Success(Unit) }
                is NetworkResult.Error -> _uiState.update { UiState.Error(r.message) }
                is NetworkResult.Exception -> _uiState.update { UiState.Error(r.throwable.message ?: "Erreur") }
            }
        }
    }
}
