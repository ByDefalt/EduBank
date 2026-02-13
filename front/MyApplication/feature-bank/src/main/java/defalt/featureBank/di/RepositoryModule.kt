package defalt.featureBank.di

import defalt.featureBank.repository.impl.BankAccountParameterRepository
import defalt.featureBank.repository.impl.BankAccountRepository
import defalt.featureBank.repository.impl.TypeRepository
import defalt.featureBank.repository.service.IBankAccountParameterRepository
import defalt.featureBank.repository.service.IBankAccountRepository
import defalt.featureBank.repository.service.ITypeRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IBankAccountRepository> { BankAccountRepository(get()) }
        single<IBankAccountParameterRepository> { BankAccountParameterRepository(get()) }
        single<ITypeRepository> { TypeRepository(get()) }
    }
