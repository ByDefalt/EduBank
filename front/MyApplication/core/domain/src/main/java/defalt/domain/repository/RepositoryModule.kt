package defalt.domain.repository

import defalt.domain.repository.impl.BankRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { BankRepository(get()) }
}
