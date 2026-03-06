package defalt.domain.entity.account

data class AccountRegister(
    val personalInfo: PersonalInformationRegister,
    val role: RoleEnum,
    val password: String,
)
