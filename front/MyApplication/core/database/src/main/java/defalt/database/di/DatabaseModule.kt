package defalt.database.di

import defalt.database.account.AccountLocalDataSource
import defalt.domain.datasource.account.IAccountLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single<IAccountLocalDataSource> { AccountLocalDataSource(androidApplication()) }
}
