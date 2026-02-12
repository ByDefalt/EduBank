package defalt.core.api.account.model

data class PersonalInformationRegisterEntity(
    val firstname: String,
    val lastname: String,
    val email: String,
    val address: String? = null,
    val phoneNumber: String? = null,

)
