package defalt.network.utils

import defalt.utils.NetworkResult
import defalt.utils.logger.ConsoleLogger
import defalt.utils.logger.LogLevel
import org.json.JSONObject
import retrofit2.Response

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>,
): NetworkResult<T> {
    val logger = ConsoleLogger(LogLevel.DEBUG)
    return try {
        val response = call()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                logger.debug(body.toString())
                NetworkResult.Success(body)
            } else if (response.code() == 204) {
                logger.debug("No content")
                @Suppress("UNCHECKED_CAST")
                NetworkResult.Success(Unit as T)
            } else {
                logger.debug("Empty body")
                NetworkResult.Error(response.code(), "Empty body")
            }
        } else {
            logger.debug(response.errorBody()?.string() ?: "Unknown error")
            NetworkResult.Error(response.code(), parseErrorMessage(response))
        }
    } catch (e: Throwable) {
        logger.debug(e.toString())
        NetworkResult.Exception(e)
    }
}

/**
 * Variante pour les endpoints retournant une liste.
 * Un 404 est traité comme un succès avec liste vide plutôt qu'une erreur.
 */
suspend fun <T> safeApiCallList(
    emptyValue: T,
    call: suspend () -> Response<T>,
): NetworkResult<T> {
    val result = safeApiCall(call)
    return if (result is NetworkResult.Error && result.code == 404) {
        NetworkResult.Success(emptyValue)
    } else {
        result
    }
}

private fun parseErrorMessage(response: Response<*>): String {
    val raw = response.errorBody()?.string()
    return when {
        response.code() >= 500 -> "Une erreur serveur s'est produite. Réessayez plus tard."
        else -> raw?.let {
            runCatching { JSONObject(it).getString("message") }.getOrNull()
        } ?: raw ?: "Unknown error"
    }
}
