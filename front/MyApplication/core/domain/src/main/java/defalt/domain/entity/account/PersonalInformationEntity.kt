package defalt.domain.entity.account

/**
 * Domain entity mirroring network PersonalInformation DTO
 */
data class PersonalInformationEntity(
    val id: Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null
)

