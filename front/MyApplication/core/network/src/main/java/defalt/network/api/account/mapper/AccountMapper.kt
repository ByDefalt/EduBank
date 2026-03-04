// defalt.network.api.account.mapper.AccountMapper.kt
package defalt.network.api.account.mapper

import defalt.domain.entity.account.AccountEntity
import defalt.domain.entity.account.AccountRegisterEntity
import defalt.domain.entity.account.PersonalInformationRegisterEntity
import defalt.network.api.account.model.Account
import defalt.network.api.account.model.AccountRegister
import defalt.network.api.account.model.PersonalInformationRegister

fun Account.toDomain(): AccountEntity = AccountEntity(
    id = this.id,
    personalInfoId = this.personalInfoId,
    roleId = this.roleId,
    state = this.state,
)

fun PersonalInformationRegisterEntity.toDto(): PersonalInformationRegister =
    PersonalInformationRegister(
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        address = this.address,
        phoneNumber = this.phoneNumber,
    )

fun AccountRegisterEntity.toDto(): AccountRegister = AccountRegister(
    personalInfo = this.personalInfo.toDto(),
    roleId = this.roleId,
    password = this.password,
)
