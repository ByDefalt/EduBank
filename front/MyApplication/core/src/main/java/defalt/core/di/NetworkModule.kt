package defalt.core.di

import defalt.core.BuildConfig.GATEWAY_URL
import defalt.core.infrastructure.ApiClient
import defalt.core.utils.createService
import org.koin.dsl.module
import defalt.core.api.account.service.DefaultApi as AccountApi

val networkModule = module {

    // 1 ApiClient gateway unique
    single { ApiClient(baseUrl = GATEWAY_URL).setLogger { println(it) } }

    // Services exposés via le même client
    single { get<ApiClient>().createService<AccountApi>() }
}
