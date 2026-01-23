package defalt.core.api.account.model

import defalt.core.api.account.model.PersonalInformationRegister

data class AccountRegisterEntity (
    val personalInfo: PersonalInformationRegister,
    val roleId: Int,
    val password: String,
    val state: String? = null

) {

}
