package defalt.featureAccount.usecase

import org.koin.dsl.module

val featureAccountUseCaseModule = module {
    single { RegisterClientAccountUseCase(get(), get()) }
    single { SignInClientAccountUseCase(get(), get()) }
}
