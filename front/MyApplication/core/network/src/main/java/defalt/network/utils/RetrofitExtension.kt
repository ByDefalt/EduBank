package defalt.network.utils

import defalt.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>,
): NetworkResult<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                NetworkResult.Success(body)
            } else {
                NetworkResult.Error(response.code(), "Empty body")
            }
        } else {
            NetworkResult.Error(response.code(), parseErrorMessage(response))
        }
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}

/**
 * Variante pour les endpoints retournant une liste.
 * Un 404 est traité comme un succès avec liste vide plutôt qu'une erreur.
 */
suspend fun <T> safeApiCallList(
    call: suspend () -> Response<T>,
    emptyValue: T,
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
