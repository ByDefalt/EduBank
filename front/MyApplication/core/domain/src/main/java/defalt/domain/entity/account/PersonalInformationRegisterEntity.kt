package defalt.domain.entity.account

/**
 * Domain entity for personal information used during registration
 */
data class PersonalInformationRegisterEntity(
    val firstname: String,
    val lastname: String,
    val email: String,
    val address: String? = null,
    val phoneNumber: String? = null
)

