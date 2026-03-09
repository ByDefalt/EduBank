package defalt.network.fake

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.datasource.operation.IOperationRemoteDataSource
import org.koin.dsl.module

val fakeDataSourcesModule = module {
    single<IAccountRemoteDataSource> {
        FakeAccountRemoteDataSource(get())
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
