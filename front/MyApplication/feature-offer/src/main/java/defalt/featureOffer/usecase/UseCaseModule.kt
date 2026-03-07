package defalt.featureOffer.usecase

import org.koin.dsl.module

val featureOfferUseCaseModule = module {
    single { GetAllOffersUseCase(get(), get()) }
    single { GetOfferByIdUseCase(get(), get()) }
    single { CreateOfferUseCase(get(), get()) }
    single { UpdateOfferUseCase(get(), get()) }
    single { DeleteOfferUseCase(get(), get()) }
}
