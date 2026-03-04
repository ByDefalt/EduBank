package defalt.domain.entity.account

import defalt.domain.entity.account.PersonalInformationRegisterEntity

/**
 * Domain entity for account registration
 */
data class AccountRegisterEntity(
    val personalInfo: PersonalInformationRegisterEntity,
    val roleId: Int,
    val password: String
)

