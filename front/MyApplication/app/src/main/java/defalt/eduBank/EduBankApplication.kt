package defalt.eduBank

import android.app.Application
import defalt.eduBank.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EduBankApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@EduBankApplication)
            modules(*appModule.toTypedArray())
        }
    }
}
