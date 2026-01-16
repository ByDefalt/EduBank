package defalt.core.di

import defalt.core.infrastructure.ApiClient
import defalt.core.utils.createService
import org.koin.core.qualifier.named
import org.koin.dsl.module

inline fun <reified S> networkModule(apiBaseUrl: String, apiName: String) = module {

    single(named(apiName)) {
        ApiClient(baseUrl = apiBaseUrl)
            .setLogger { message -> println(message) }
    }

    single {
        get<ApiClient>(named(apiName)).createService<S>()
    }
}
