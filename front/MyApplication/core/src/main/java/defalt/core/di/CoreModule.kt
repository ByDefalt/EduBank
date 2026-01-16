package defalt.core.di

import defalt.core.BuildConfig.GATEWAY_NAME
import defalt.core.BuildConfig.GATEWAY_URL
import defalt.core.api.account.service.DefaultApi as AccountApi

val coreModule = listOf(
    networkModule<AccountApi>(GATEWAY_URL, GATEWAY_NAME),

)