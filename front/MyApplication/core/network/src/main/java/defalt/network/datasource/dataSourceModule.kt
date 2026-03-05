package defalt.network.datasource

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.network.datasource.account.AccountRemoteDataSource
import org.koin.dsl.module

val dataSourcesModule = module {
    single<IAccountRemoteDataSource> { AccountRemoteDataSource(get()) }
}