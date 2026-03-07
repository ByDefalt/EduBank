package defalt.featureBank.usecase

import org.koin.dsl.module

val featureBankUseCaseModule = module {
    single { GetHomeAccount(get(), get()) }
}
