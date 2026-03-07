package defalt.featureBank.usecase

import org.koin.dsl.module

val featureBankUseCaseModule = module {
    single { GetHomeData(get(), get(), get(), get()) }
    single { GetAllMyAccount(get(), get()) }
}
