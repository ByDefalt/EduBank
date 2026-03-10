package defalt.network.mapper.bank

import defalt.domain.entity.bank.BankAccount as BankAccountEntity
import defalt.domain.entity.bank.BankAccountCreateRequest as BankAccountCreateRequestEntity
import defalt.domain.entity.bank.BankAccountDetail as BankAccountDetailEntity
import defalt.domain.entity.bank.BankAccountParameter as BankAccountParameterEntity
import defalt.domain.entity.bank.BankAccountPivot as BankAccountPivotEntity
import defalt.domain.entity.bank.State as StateEntity
import defalt.domain.entity.bank.Type as TypeEntity
import defalt.network.api.bank.model.BankAccount as BankAccountDto
import defalt.network.api.bank.model.BankAccountCreateRequest as BankAccountCreateRequestDto
import defalt.network.api.bank.model.BankAccountDetail as BankAccountDetailDto
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto
import defalt.network.api.bank.model.BankAccountPivot as BankAccountPivotDto
import defalt.network.api.bank.model.State as StateDto
import defalt.network.api.bank.model.Type as TypeDto

// ── BankAccount ───────────────────────────────────────────────────────────────

fun BankAccountDto.toEntity(): BankAccountEntity = BankAccountEntity(
    id = this.id,
    parameterId = this.parameterId,
    typeId = this.typeId,
    sold = this.sold,
    iban = this.iban,
)

fun BankAccountEntity.toDto(): BankAccountDto = BankAccountDto(
    id = this.id,
    parameterId = this.parameterId,
    typeId = this.typeId,
    sold = this.sold,
    iban = this.iban,
)

@JvmName("bankAccountListToEntity")
fun List<BankAccountDto>.toEntity(): List<BankAccountEntity> = this.map { it.toEntity() }

// ── BankAccountDetail ─────────────────────────────────────────────────────────

fun BankAccountDetailDto.toEntity(): BankAccountDetailEntity = BankAccountDetailEntity(
    id = this.id,
    parameter = this.parameter?.toEntity(),
    type = this.type?.toEntity(),
    sold = this.sold,
    iban = this.iban,
)

fun BankAccountDetailEntity.toDto(): BankAccountDetailDto = BankAccountDetailDto(
    id = this.id,
    parameter = this.parameter?.toDto(),
    type = this.type?.toDto(),
    sold = this.sold,
    iban = this.iban,
)

fun BankAccountDetailDto.toBankAccountEntity(): BankAccountEntity = BankAccountEntity(
    id = this.id,
    parameterId = this.parameter?.id,
    typeId = this.type?.id,
    sold = this.sold,
    iban = this.iban,
)

@JvmName("bankAccountDetailListToEntity")
fun List<BankAccountDetailDto>.toEntity(): List<BankAccountDetailEntity> = this.map { it.toEntity() }

@JvmName("bankAccountDetailListToBankAccountEntity")
fun List<BankAccountDetailDto>.toBankAccountEntity(): List<BankAccountEntity> = this.map { it.toBankAccountEntity() }

// ── BankAccountCreateRequest ──────────────────────────────────────────────────

fun BankAccountCreateRequestEntity.toDto(): BankAccountCreateRequestDto = BankAccountCreateRequestDto(
    typeId = this.typeId,
    iban = this.iban,
    sold = this.sold,
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toDto(),
)

// ── BankAccountParameter ──────────────────────────────────────────────────────

fun BankAccountParameterDto.toEntity(): BankAccountParameterEntity = BankAccountParameterEntity(
    id = this.id,
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toEntity(),
)

fun BankAccountParameterEntity.toDto(): BankAccountParameterDto = BankAccountParameterDto(
    id = this.id,
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toDto(),
)

// ── State ─────────────────────────────────────────────────────────────────────

fun StateDto.toEntity(): StateEntity = when (this) {
    StateDto.ACTIVE -> StateEntity.ACTIVE
    StateDto.INACTIVE -> StateEntity.INACTIVE
    StateDto.BLOQUED -> StateEntity.BLOQUED
    StateDto.CLOSED -> StateEntity.CLOSED
}

fun StateEntity.toDto(): StateDto = when (this) {
    StateEntity.ACTIVE -> StateDto.ACTIVE
    StateEntity.INACTIVE -> StateDto.INACTIVE
    StateEntity.BLOQUED -> StateDto.BLOQUED
    StateEntity.CLOSED -> StateDto.CLOSED
}

// ── Type ──────────────────────────────────────────────────────────────────────

fun TypeDto.toEntity(): TypeEntity = TypeEntity(
    id = this.id,
    name = this.name,
)

fun TypeEntity.toDto(): TypeDto = TypeDto(
    id = this.id,
    name = this.name,
)

@JvmName("typeListToEntity")
fun List<TypeDto>.toEntity(): List<TypeEntity> = this.map { it.toEntity() }

@JvmName("typeListToDto")
fun List<TypeEntity>.toDto(): List<TypeDto> = this.map { it.toDto() }


fun BankAccountPivotDto.toEntity(): BankAccountPivotEntity = BankAccountPivotEntity(
    bankAccountId = this.bankAccountId,
    accountId = this.accountId
)

fun BankAccountPivotEntity.toDto(): BankAccountPivotDto = BankAccountPivotDto(
    bankAccountId = this.bankAccountId,
    accountId = this.accountId
)

@JvmName("bankAccountPivotListToEntity")
fun List<BankAccountPivotDto>.toEntity(): List<BankAccountPivotEntity> = this.map { it.toEntity() }

@JvmName("bankAccountPivotListToDto")
fun List<BankAccountPivotEntity>.toDto(): List<BankAccountPivotDto> = this.map { it.toDto() }