package defalt.featureBank.di

import defalt.featureBank.viewModel.AccountDetailsViewModel
import defalt.featureBank.viewModel.HomeAccountViewModel
import defalt.featureBank.viewModel.ListAccountViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { ListAccountViewModel() }
        viewModel { AccountDetailsViewModel(get()) }
        viewModel { HomeAccountViewModel() }
    }
