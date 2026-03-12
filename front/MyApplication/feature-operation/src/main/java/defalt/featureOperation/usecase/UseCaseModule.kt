package defalt.featureOperation.usecase

import org.koin.dsl.module

val featureOperationUseCaseModule = module {
    single { GetMyBeneficiaries(get(), get(), get()) }
    single { AddBeneficiary(get(), get(), get()) }
    single { EditBeneficiary(get(), get()) }
    single { DeleteBeneficiary(get(), get()) }
    single { GetAllMyAccount(get(), get()) }
    single { CreateTransfer(get(), get()) }

    single { GetAllOperationsUseCase(get(), get()) }
    single { GetOperationByIdUseCase(get(), get()) }
    single { CancelOperationUseCase(get(), get()) }
    single { UpdateOperationStateUseCase(get(), get()) }
}
