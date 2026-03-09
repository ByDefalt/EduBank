package defalt.network.utils

import defalt.network.infrastructure.ApiClient

inline fun <reified S> ApiClient.createService(): S =
    createService(S::class.java)
