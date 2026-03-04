package defalt.domain.entity.account

/**
 * Domain entity mirroring network SignInRequest DTO
 */
data class SignInRequestEntity(
    val username: String,
    val password: String
)

