package defalt.feature_operation.di

import defalt.feature_operation.repository.impl.BeneficiaryRepository
import defalt.feature_operation.repository.impl.OperationRepository
import defalt.feature_operation.repository.service.IBeneficiaryRepository
import defalt.feature_operation.repository.service.IOperationRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IOperationRepository> { OperationRepository(get()) }
        single<IBeneficiaryRepository> { BeneficiaryRepository(get()) }

    }
