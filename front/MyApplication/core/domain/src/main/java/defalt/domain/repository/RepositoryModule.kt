package defalt.domain.repository

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.domain.repository.impl.AccountRepository
import defalt.domain.repository.impl.BankRepository
import defalt.domain.repository.impl.OfferRepository
import defalt.domain.repository.impl.OperationRepository
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.repository.service.IBankRepository
import defalt.domain.repository.service.IOfferRepository
import defalt.domain.repository.service.IOperationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IAccountRepository> { AccountRepository(get<IAccountRemoteDataSource>()) }
    single<IBankRepository> { BankRepository(get<IBankRemoteDataSource>()) }
    single<IOfferRepository> { OfferRepository(get<IOfferRemoteDataSource>()) }
    single<IOperationRepository> { OperationRepository(get<IOperationRemoteDataSource>()) }
}
