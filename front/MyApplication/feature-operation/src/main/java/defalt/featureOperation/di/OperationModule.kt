package defalt.featureOperation.di

import defalt.featureOperation.usecase.featureOperationUseCaseModule
import defalt.featureOperation.viewModel.featureOperationViewModelModule

/** Module racine de feature-operation */
val operationModule = listOf(featureOperationUseCaseModule, featureOperationViewModelModule)
