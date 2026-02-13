package defalt.featureOffer.di

import defalt.featureOffer.repository.impl.OfferRepository
import defalt.featureOffer.repository.service.IOfferRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IOfferRepository> { OfferRepository(get()) }
    }
