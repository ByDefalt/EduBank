package defalt.featureBank.di

import defalt.featureBank.usecase.featureBankUseCaseModule
import defalt.featureBank.viewModel.featureBankViewModelModule

/** Module racine de feature-bank */
val bankModule = listOf(featureBankUseCaseModule, featureBankViewModelModule)
