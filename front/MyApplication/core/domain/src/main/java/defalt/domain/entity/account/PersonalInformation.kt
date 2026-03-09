package defalt.domain.entity.account

data class PersonalInformation(
    val id: Int? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val email: String? = null,
    val address: String? = null,
    val phoneNumber: String? = null,
)
