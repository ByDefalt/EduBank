package defalt.featureOperation.usecase

import org.koin.dsl.module

val featureOperationUseCaseModule = module {
    single { GetMyBeneficiaries(get(), get(), get()) }
    single { AddBeneficiary(get(), get(), get()) }
    single { EditBeneficiary(get(), get()) }
    single { DeleteBeneficiary(get(), get()) }
    single { GetMyBankAccounts(get(), get()) }
    single { CreateTransfer(get(), get(), get()) }
    // Admin
    single { GetAllOperationsUseCase(get(), get()) }
    single { GetOperationByIdUseCase(get(), get()) }
    single { CancelOperationUseCase(get(), get()) }
    single { UpdateOperationStateUseCase(get(), get()) }
}
