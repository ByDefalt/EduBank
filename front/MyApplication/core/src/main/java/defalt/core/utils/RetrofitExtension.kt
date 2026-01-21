package defalt.core.utils

suspend fun <T> safeApiCall(
    call: suspend () -> retrofit2.Response<T>
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
                response.errorBody()?.string()
            )
        }
    } catch (e: Throwable) {
        NetworkResult.Exception(e)
    }
}
