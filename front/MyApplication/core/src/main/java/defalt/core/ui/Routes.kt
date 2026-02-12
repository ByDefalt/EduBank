package defalt.core.ui

import kotlinx.serialization.Serializable

@Serializable sealed class Routes {
    @Serializable object Home : Routes()
    @Serializable object Account : Routes() {
        @Serializable object Login : Routes()
        @Serializable object Register : Routes()
    }
}