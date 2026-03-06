package defalt.database.di

import androidx.room.Room
import defalt.database.AppDatabase
import defalt.database.account.AccountLocalDataSource
import defalt.domain.datasource.account.IAccountLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "edubank.db",
        ).build()
    }

    single { get<AppDatabase>().tokenDao() }

    single<IAccountLocalDataSource> { AccountLocalDataSource(get()) }
}
