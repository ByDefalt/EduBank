package defalt.featureOffer.viewModel

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureOfferViewModelModule = module {
    viewModel { OffersViewModel() }
}
