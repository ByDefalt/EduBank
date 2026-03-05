package defalt.network.mapper.account

import defalt.domain.entity.account.Account as AccountEntity
import defalt.network.api.account.model.Account as AccountDto

import defalt.domain.entity.account.AccountRegister as AccountRegisterEntity
import defalt.network.api.account.model.AccountRegister as AccountRegisterDto

import defalt.network.api.account.model.PersonalInformationRegister as PersonalInformationRegisterDto
import defalt.domain.entity.account.PersonalInformationRegister as PersonalInformationRegisterEntity




fun AccountDto.toEntity(): AccountEntity {
    return AccountEntity(
        id = this.id,
        personalInfoId = this.personalInfoId,
        roleId = this.roleId,
        state = this.state,
    )
}

fun AccountEntity.toDto(): AccountDto {
    return AccountDto(
        id = this.id,
        personalInfoId = this.personalInfoId,
        roleId = this.roleId,
        state = this.state
    )
}

fun List<AccountDto>.toEntity(): List<AccountEntity> = this.map { it.toEntity() }



fun AccountRegisterEntity.toDto(): AccountRegisterDto {
    return AccountRegisterDto(
        personalInfo = this.personalInfo.toDto(),
        roleId = this.roleId,
        password = this.password,
    )
}

fun AccountRegisterDto.toEntity(): AccountRegisterEntity {
    return AccountRegisterEntity(
        personalInfo = this.personalInfo.toEntity(),
        roleId = this.roleId,
        password = this.password,
    )
}


fun PersonalInformationRegisterEntity.toDto(): PersonalInformationRegisterDto {
    return PersonalInformationRegisterDto(
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        address = this.address,
        phoneNumber = this.phoneNumber
    )
}

fun PersonalInformationRegisterDto.toEntity(): PersonalInformationRegisterEntity{
    return PersonalInformationRegisterEntity(
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        address = this.address
    )
}
