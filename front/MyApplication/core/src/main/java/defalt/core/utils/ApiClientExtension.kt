package defalt.core.utils

import defalt.core.infrastructure.ApiClient

inline fun <reified S> ApiClient.createService(): S =
    createService(S::class.java)
