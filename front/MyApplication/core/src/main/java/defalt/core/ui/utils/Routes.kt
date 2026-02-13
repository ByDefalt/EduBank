package defalt.core.ui.utils

import kotlinx.serialization.Serializable

@Serializable sealed class Routes {

    @Serializable object Core : Routes() {

        @Serializable object Home : Routes()
    }


    @Serializable object Account : Routes() {
        @Serializable object Login : Routes()

        @Serializable object Register : Routes()
    }

    @Serializable object Bank : Routes() {
    }

    @Serializable object Offer : Routes() {
        @Serializable object List : Routes()
    }
    @Serializable object Operation : Routes() {
    }

}
