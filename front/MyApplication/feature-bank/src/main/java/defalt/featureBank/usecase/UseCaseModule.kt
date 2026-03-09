package defalt.featureBank.usecase

import org.koin.dsl.module

val featureBankUseCaseModule = module {
    single { GetHomeData(get(), get(), get(), get()) }
    single { GetAllMyAccount(get(), get()) }
    single { GetAccountDetailsAndOperation(get(), get(), get(), get()) }

    single { AdminGetAllBankAccountsUseCase(get(), get()) }
    single { AdminGetBankAccountByIdUseCase(get(), get()) }
    single { AdminDeleteBankAccountUseCase(get(), get()) }
    single { AdminUpdateBankAccountParamUseCase(get(), get()) }
    single { AdminCreateBankAccountUseCase(get(), get()) }
    single { AdminUpdateBankAccountUseCase(get(), get()) }
    single { AdminGetAllAccountsForBankUseCase(get(), get()) }
}
