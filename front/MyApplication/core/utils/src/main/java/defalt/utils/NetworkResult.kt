package defalt.utils

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(
        val code: Int,
        val message: String,
    ) : NetworkResult<Nothing>()

    data class Exception(val throwable: Throwable) : NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[code=$code, message=$message]"
            is Exception -> "Exception[throwable=$throwable]"
        }
    }
}
