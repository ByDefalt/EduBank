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
            val raw = response.errorBody()?.string()
            val message = when {
                response.code() >= 500 -> "Une erreur serveur s'est produite. Réessayez plus tard."
                else -> raw?.let {
                    runCatching { JSONObject(it).getString("message") }.getOrNull()
                } ?: raw ?: "Unknown error"
            }
            NetworkResult.Error(response.code(), message)
        }
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}
