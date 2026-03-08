package defalt.featureAccount.usecase

import org.koin.dsl.module

val featureAccountUseCaseModule = module {
    single { RegisterClientAccountUseCase(get(), get()) }
    single { SignInClientAccountUseCase(get(), get(), get()) }
    single { LogoutUseCase(get()) }

    single { GetAllAccountsUseCase(get(), get()) }
    single { GetAccountByIdUseCase(get(), get()) }
    single { ActivateAccountUseCase(get(), get()) }
    single { DeactivateAccountUseCase(get(), get()) }
    single { UpdatePersonalInfoUseCase(get(), get()) }
}
