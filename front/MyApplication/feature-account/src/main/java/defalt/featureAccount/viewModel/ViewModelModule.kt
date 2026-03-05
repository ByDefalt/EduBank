package defalt.featureAccount.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureAccountViewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}
