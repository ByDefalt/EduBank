package defalt.domain.entity.account

data class AccountRegisterEntity(
    val personalInfo: PersonalInformationRegisterEntity,
    val roleId: Int,
    val password: String,
    val state: String? = null,

)
