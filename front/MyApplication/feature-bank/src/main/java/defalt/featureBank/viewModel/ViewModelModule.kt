package defalt.featureBank.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureBankViewModelModule = module {
    viewModel { ListAccountViewModel() }
    viewModel { AccountDetailsViewModel(get()) }
    viewModel { HomeAccountViewModel() }
}
