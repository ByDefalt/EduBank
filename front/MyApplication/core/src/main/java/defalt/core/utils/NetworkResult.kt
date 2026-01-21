package defalt.core.utils

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int,
        val message: String? = null
    ) : NetworkResult<Nothing>()
    data class Exception(val throwable: Throwable) : NetworkResult<Nothing>()
}
