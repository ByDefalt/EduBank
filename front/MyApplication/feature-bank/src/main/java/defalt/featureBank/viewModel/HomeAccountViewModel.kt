package defalt.featureBank.viewModel

import androidx.lifecycle.ViewModel
import defalt.domain.entity.bank.HomeData
import defalt.featureBank.usecase.GetHomeData
import defalt.ui.state.UiState
import defalt.ui.state.launchWithUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeAccountViewModel(
    private val getHomeData: GetHomeData,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<HomeData>>(UiState.Loading)
    val uiState: StateFlow<UiState<HomeData>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun retry() = loadData()

    private fun loadData() = launchWithUiState(stateFlow = _uiState, transform = { it }) {
        getHomeData()
    }
}
