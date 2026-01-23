package defalt.feature_bank.di

import defalt.feature_bank.repository.impl.BankAccountParameterRepository
import defalt.feature_bank.repository.impl.BankAccountRepository
import defalt.feature_bank.repository.impl.TypeRepository
import defalt.feature_bank.repository.service.IBankAccountParameterRepository
import defalt.feature_bank.repository.service.IBankAccountRepository
import defalt.feature_bank.repository.service.ITypeRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IBankAccountRepository> { BankAccountRepository(get()) }
        single<IBankAccountParameterRepository> { BankAccountParameterRepository(get()) }
        single<ITypeRepository> { TypeRepository(get()) }

    }
