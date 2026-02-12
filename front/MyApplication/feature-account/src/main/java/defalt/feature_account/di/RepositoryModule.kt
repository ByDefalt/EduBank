package defalt.feature_account.di

import defalt.feature_account.repository.impl.AccountRepository
import defalt.feature_account.repository.impl.PersonalInformationRepository
import defalt.feature_account.repository.service.IAccountRepository
import defalt.feature_account.repository.service.IPersonalInformationRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IAccountRepository> { AccountRepository(get()) }
        single<IPersonalInformationRepository> { PersonalInformationRepository(get()) }

    }
