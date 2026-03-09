package defalt.featureAccount.viewModel

import androidx.lifecycle.ViewModel
import defalt.featureAccount.usecase.LogoutUseCase

class MenuViewModel(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    fun logout() {
        logoutUseCase()
    }
}
