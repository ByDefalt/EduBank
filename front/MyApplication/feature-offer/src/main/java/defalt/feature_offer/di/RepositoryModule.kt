package defalt.feature_offer.di

import defalt.feature_offer.repository.impl.OfferRepository
import defalt.feature_offer.repository.service.IOfferRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IOfferRepository> { OfferRepository(get()) }

    }
