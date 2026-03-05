package defalt.featureBank.di

import defalt.featureBank.viewModel.ListAccountViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { ListAccountViewModel(get()) }
    }
