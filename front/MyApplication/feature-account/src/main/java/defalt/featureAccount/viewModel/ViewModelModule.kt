package defalt.featureAccount.viewModel

import defalt.featureAccount.usecase.RegisterClientAccountUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureAccountViewModelModule = module {
    single { RegisterClientAccountUseCase(get(), get()) }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel(get()) }
}
