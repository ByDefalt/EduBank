package defalt.featureAccount.di

import defalt.featureAccount.usecase.featureAccountUseCaseModule
import defalt.featureAccount.viewModel.featureAccountViewModelModule

/** Module racine de feature-account */
val accountModule = listOf(featureAccountUseCaseModule, featureAccountViewModelModule)
