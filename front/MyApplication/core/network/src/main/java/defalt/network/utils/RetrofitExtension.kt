package defalt.network.utils

import defalt.utils.NetworkResult
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
            NetworkResult.Error(
                response.code(),
                response.errorBody()?.string() ?: "Unknown error",
            )
        }
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}
