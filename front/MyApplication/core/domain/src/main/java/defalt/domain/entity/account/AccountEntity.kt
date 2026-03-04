package defalt.domain.entity.account

import kotlinx.serialization.Serializable

/**
 * Domain entity mirroring network Account DTO
 */
data class AccountEntity(
    val id: String? = null,
    val personalInfoId: Int? = null,
    val roleId: Int? = null,
    val state: String? = null
)

