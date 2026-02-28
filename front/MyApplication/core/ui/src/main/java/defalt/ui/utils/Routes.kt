package defalt.ui.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    object Core : Routes() {

        @Serializable
        object Home : Routes()
    }

    @Serializable
    object Account : Routes() {
        @Serializable
        object Login : Routes()

        @Serializable
        object Register : Routes()
    }

    @Serializable
    object Bank : Routes() {
        @Serializable
        object Home : Routes()

        @Serializable
        object ListAccount : Routes()
    }

    @Serializable
    object Offer : Routes() {
        @Serializable
        object List : Routes()
    }

    @Serializable
    object Operation : Routes() {
        @Serializable
        object Transfer : Routes()
    }
}
