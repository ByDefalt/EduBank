package defalt.featureAccount.di

import defalt.featureAccount.repository.impl.AccountRepository
import defalt.featureAccount.repository.impl.PersonalInformationRepository
import defalt.featureAccount.repository.service.IAccountRepository
import defalt.featureAccount.repository.service.IPersonalInformationRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        single<IAccountRepository> { AccountRepository(get()) }
        single<IPersonalInformationRepository> { PersonalInformationRepository(get()) }
    }
