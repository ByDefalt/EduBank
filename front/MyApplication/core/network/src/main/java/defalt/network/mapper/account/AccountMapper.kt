package defalt.network.mapper.account

import defalt.domain.entity.account.Account as AccountEntity
import defalt.domain.entity.account.AccountRegister as AccountRegisterEntity
import defalt.domain.entity.account.AccountStateEnum as AccountStateEnumEntity
import defalt.domain.entity.account.Error as ErrorEntity
import defalt.domain.entity.account.PersonalInformation as PersonalInformationEntity
import defalt.domain.entity.account.PersonalInformationRegister as PersonalInformationRegisterEntity
import defalt.domain.entity.account.Role as RoleEntity
import defalt.domain.entity.account.RoleEnum as RoleEnumEntity
import defalt.domain.entity.account.SignInRequest as SignInRequestEntity
import defalt.domain.entity.account.TokenRequest as TokenRequestEntity
import defalt.domain.entity.account.TokenResponse as TokenResponseEntity
import defalt.network.api.account.model.Account as AccountDto
import defalt.network.api.account.model.AccountRegister as AccountRegisterDto
import defalt.network.api.account.model.AccountStateEnum as AccountStateEnumDto
import defalt.network.api.account.model.Error as ErrorDto
import defalt.network.api.account.model.PersonalInformation as PersonalInformationDto
import defalt.network.api.account.model.PersonalInformationRegister as PersonalInformationRegisterDto
import defalt.network.api.account.model.Role as RoleDto
import defalt.network.api.account.model.RoleEnum as RoleEnumDto
import defalt.network.api.account.model.SignInRequest as SignInRequestDto
import defalt.network.api.account.model.TokenRequest as TokenRequestDto
import defalt.network.api.account.model.TokenResponse as TokenResponseDto

// ── AccountStateEnum ──────────────────────────────────────────────────────────

fun AccountStateEnumDto.toEntity(): AccountStateEnumEntity = when (this) {
    AccountStateEnumDto.ACTIVE -> AccountStateEnumEntity.ACTIVE
    AccountStateEnumDto.INACTIVE -> AccountStateEnumEntity.INACTIVE
    AccountStateEnumDto.ENCLOSE -> AccountStateEnumEntity.ENCLOSE
}

fun AccountStateEnumEntity.toDto(): AccountStateEnumDto = when (this) {
    AccountStateEnumEntity.ACTIVE -> AccountStateEnumDto.ACTIVE
    AccountStateEnumEntity.INACTIVE -> AccountStateEnumDto.INACTIVE
    AccountStateEnumEntity.ENCLOSE -> AccountStateEnumDto.ENCLOSE
}

// ── RoleEnum ──────────────────────────────────────────────────────────────────

fun RoleEnumDto.toEntity(): RoleEnumEntity = when (this) {
    RoleEnumDto.ADMIN -> RoleEnumEntity.ADMIN
    RoleEnumDto.CUSTOMER -> RoleEnumEntity.CUSTOMER
}

fun RoleEnumEntity.toDto(): RoleEnumDto = when (this) {
    RoleEnumEntity.ADMIN -> RoleEnumDto.ADMIN
    RoleEnumEntity.CUSTOMER -> RoleEnumDto.CUSTOMER
}

// ── Account ───────────────────────────────────────────────────────────────────

fun AccountDto.toEntity(): AccountEntity = AccountEntity(
    id = this.id,
    personalInfoId = this.personalInfoId,
    roleId = this.roleId,
    state = this.state?.toEntity(),
)

fun AccountEntity.toDto(): AccountDto = AccountDto(
    id = this.id,
    personalInfoId = this.personalInfoId,
    roleId = this.roleId,
    state = this.state?.toDto(),
)

@JvmName("accountListToEntity")
fun List<AccountDto>.toEntity(): List<AccountEntity> = this.map { it.toEntity() }

// ── AccountRegister ───────────────────────────────────────────────────────────

fun AccountRegisterEntity.toDto(): AccountRegisterDto = AccountRegisterDto(
    personalInfo = this.personalInfo.toDto(),
    role = this.role.toDto(),
    password = this.password,
)

fun AccountRegisterDto.toEntity(): AccountRegisterEntity = AccountRegisterEntity(
    personalInfo = this.personalInfo.toEntity(),
    role = this.role.toEntity(),
    password = this.password,
)

// ── Error ─────────────────────────────────────────────────────────────────────

fun ErrorDto.toEntity(): ErrorEntity = ErrorEntity(
    code = this.code,
    message = this.message,
    details = this.details,
)

fun ErrorEntity.toDto(): ErrorDto = ErrorDto(
    code = this.code,
    message = this.message,
    details = this.details,
)

// ── PersonalInformation ───────────────────────────────────────────────────────

fun PersonalInformationDto.toEntity(): PersonalInformationEntity = PersonalInformationEntity(
    id = this.id,
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
    address = this.address,
    phoneNumber = this.phoneNumber,
)

fun PersonalInformationEntity.toDto(): PersonalInformationDto = PersonalInformationDto(
    id = this.id,
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
    address = this.address,
    phoneNumber = this.phoneNumber,
)

@JvmName("personalInformationListToEntity")
fun List<PersonalInformationDto>.toEntity(): List<PersonalInformationEntity> = this.map { it.toEntity() }

// ── PersonalInformationRegister ───────────────────────────────────────────────

fun PersonalInformationRegisterEntity.toDto(): PersonalInformationRegisterDto = PersonalInformationRegisterDto(
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
    address = this.address,
    phoneNumber = this.phoneNumber,
)

fun PersonalInformationRegisterDto.toEntity(): PersonalInformationRegisterEntity = PersonalInformationRegisterEntity(
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
    address = this.address,
    phoneNumber = this.phoneNumber,
)

// ── Role ──────────────────────────────────────────────────────────────────────

fun RoleDto.toEntity(): RoleEntity = RoleEntity(
    id = this.id,
    name = this.name,
)

fun RoleEntity.toDto(): RoleDto = RoleDto(
    id = this.id,
    name = this.name,
)

@JvmName("roleListToEntity")
fun List<RoleDto>.toEntity(): List<RoleEntity> = this.map { it.toEntity() }

// ── SignInRequest ─────────────────────────────────────────────────────────────

fun SignInRequestEntity.toDto(): SignInRequestDto = SignInRequestDto(
    id = this.id,
    password = this.password,
)

fun SignInRequestDto.toEntity(): SignInRequestEntity = SignInRequestEntity(
    id = this.id,
    password = this.password,
)

// ── TokenRequest ──────────────────────────────────────────────────────────────

fun TokenRequestEntity.toDto(): TokenRequestDto = TokenRequestDto(jwt = this.jwt)

fun TokenRequestDto.toEntity(): TokenRequestEntity = TokenRequestEntity(jwt = this.jwt)

// ── TokenResponse ─────────────────────────────────────────────────────────────

fun TokenResponseDto.toEntity(): TokenResponseEntity = TokenResponseEntity(
    id = this.id,
    role = this.role,
)

fun TokenResponseEntity.toDto(): TokenResponseDto = TokenResponseDto(
    id = this.id,
    role = this.role,
)
