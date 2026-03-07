package defalt.domain.entity.account

data class Error(
    val code: String? = null,
    val message: String? = null,
    val details: String? = null,
)
