package defalt.featureAccount.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureAccountViewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    // Admin
    viewModel { AdminAccountListViewModel(get()) }
    viewModel { AdminAccountDetailViewModel(get(), get(), get()) }
}
