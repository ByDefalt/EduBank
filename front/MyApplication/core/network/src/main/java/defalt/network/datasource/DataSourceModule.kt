package defalt.network.datasource

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.datasource.operation.IOperationRemoteDataSource
import defalt.network.datasource.account.AccountRemoteDataSource
import defalt.network.datasource.bank.BankRemoteDataSource
import defalt.network.datasource.offer.OfferRemoteDataSource
import defalt.network.datasource.operation.OperationRemoteDataSource
import org.koin.dsl.module

val dataSourcesModule = module {
    single<IAccountRemoteDataSource> {
        AccountRemoteDataSource(get(), get(), get(), get())
    }
    single<IBankRemoteDataSource> {
        BankRemoteDataSource(get(), get(), get(), get())
    }
    single<IOfferRemoteDataSource> {
        OfferRemoteDataSource(get())
    }
    single<IOperationRemoteDataSource> {
        OperationRemoteDataSource(get(), get())
    }
}
