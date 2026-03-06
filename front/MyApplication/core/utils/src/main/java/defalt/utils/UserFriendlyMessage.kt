package defalt.utils

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun Throwable.toUserFriendlyMessage(): String {
    return when (this) {
        is UnknownHostException -> "Impossible de contacter le serveur. Vérifiez votre connexion internet."
        is ConnectException -> "La connexion au serveur a échoué. Réessayez plus tard."
        is SocketTimeoutException -> "Le serveur met trop de temps à répondre. Réessayez."
        is SSLException -> "Erreur de sécurité lors de la connexion."
        else -> "Une erreur inattendue s'est produite."
    }
}
