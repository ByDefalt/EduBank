package defalt.featureAccount.di

import defalt.featureAccount.viewModel.LoginViewModel
import defalt.featureAccount.viewModel.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { LoginViewModel() }
        viewModel { RegisterViewModel() }
    }

