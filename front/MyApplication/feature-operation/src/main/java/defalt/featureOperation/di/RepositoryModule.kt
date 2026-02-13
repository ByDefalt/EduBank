package defalt.featureOperation.di

import defalt.featureOperation.repository.impl.BeneficiaryRepository
import defalt.featureOperation.repository.impl.OperationRepository
import defalt.featureOperation.repository.service.IBeneficiaryRepository
import defalt.featureOperation.repository.service.IOperationRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IOperationRepository> { OperationRepository(get()) }
        single<IBeneficiaryRepository> { BeneficiaryRepository(get()) }
    }
