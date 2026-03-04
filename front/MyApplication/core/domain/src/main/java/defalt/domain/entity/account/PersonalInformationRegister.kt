package defalt.domain.entity.account

data class PersonalInformationRegister(
    val firstname: String,
    val lastname: String,
    val email: String,
    val address: String? = null,
    val phoneNumber: String? = null
)

