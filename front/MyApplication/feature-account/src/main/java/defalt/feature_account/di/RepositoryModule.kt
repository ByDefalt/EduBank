package defalt.feature_account.di

import defalt.feature_account.repository.service.AccountRepository
import defalt.feature_account.repository.impl.AccountRepositoryImpl
import defalt.feature_account.repository.service.PersonalInformationRepository
import defalt.feature_account.repository.impl.PersonalInformationRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AccountRepository> { AccountRepositoryImpl(get()) }
        single<PersonalInformationRepository> { PersonalInformationRepositoryImpl(get()) }

    }
