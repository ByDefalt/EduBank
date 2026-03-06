package defalt.network.fake

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.network.datasource.account.AccountRemoteDataSource
import defalt.network.datasource.bank.BankRemoteDataSource
import defalt.network.datasource.offer.OfferRemoteDataSource
import defalt.network.datasource.operation.OperationRemoteDataSource
import org.koin.dsl.module

val fakeDataSourcesModule = module {
    single<IAccountRemoteDataSource> {
        FakeAccountRemoteDataSource()
    }
    single<IBankRemoteDataSource> {
        FakeBankRemoteDataSource()
    }
    single<IOfferRemoteDataSource> {
        FakeOfferRemoteDataSource()
    }
    single<IOperationRemoteDataSource> {
        FakeOperationRemoteDataSource()
    }
}
