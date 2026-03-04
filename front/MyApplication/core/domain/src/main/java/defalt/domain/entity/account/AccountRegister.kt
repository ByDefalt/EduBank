package defalt.domain.entity.account

data class AccountRegister(
    val personalInfo: PersonalInformationRegister,
    val roleId: Int,
    val password: String
)

