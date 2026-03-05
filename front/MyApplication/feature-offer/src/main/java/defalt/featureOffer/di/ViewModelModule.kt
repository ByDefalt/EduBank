package defalt.featureOffer.di

import defalt.featureOffer.viewModel.OffersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { OffersViewModel() }
    }

