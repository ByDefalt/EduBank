package defalt.featureOffer.di

import defalt.featureOffer.usecase.featureOfferUseCaseModule
import defalt.featureOffer.viewModel.featureOfferViewModelModule

/** Module racine de feature-offer */
val offerModule = listOf(featureOfferUseCaseModule, featureOfferViewModelModule)
