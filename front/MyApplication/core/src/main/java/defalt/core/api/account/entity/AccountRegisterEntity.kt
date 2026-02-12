package defalt.core.api.account.model

data class AccountRegisterEntity(
    val personalInfo: PersonalInformationRegister,
    val roleId: Int,
    val password: String,
    val state: String? = null,

)
