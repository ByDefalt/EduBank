package defalt.network.mapper.bank

import defalt.domain.entity.bank.BankAccount as BankAccountEntity
import defalt.network.api.bank.model.BankAccount as BankAccountDto

import defalt.domain.entity.bank.BankAccountCreateRequest as BankAccountCreateRequestEntity
import defalt.network.api.bank.model.BankAccountCreateRequest as BankAccountCreateRequestDto

import defalt.domain.entity.bank.BankAccountDetail as BankAccountDetailEntity
import defalt.network.api.bank.model.BankAccountDetail as BankAccountDetailDto

import defalt.domain.entity.bank.BankAccountParameter as BankAccountParameterEntity
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto

import defalt.domain.entity.bank.State as StateEntity
import defalt.network.api.bank.model.State as StateDto

import defalt.domain.entity.bank.Type as TypeEntity
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

// ── BankAccountCreateRequest ──────────────────────────────────────────────────

fun BankAccountCreateRequestEntity.toDto(): BankAccountCreateRequestDto = BankAccountCreateRequestDto(
    typeId = this.typeId,
    iban = this.iban,
    sold = this.sold,
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toDto(),
)

fun BankAccountCreateRequestDto.toEntity(): BankAccountCreateRequestEntity = BankAccountCreateRequestEntity(
    typeId = this.typeId,
    iban = this.iban,
    sold = this.sold,
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toEntity(),
)

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

// ── State (enum) ──────────────────────────────────────────────────────────────

fun StateDto.toEntity(): StateEntity = when (this) {
    StateDto.ACTIVE   -> StateEntity.ACTIVE
    StateDto.INACTIVE -> StateEntity.INACTIVE
    StateDto.BLOQUED  -> StateEntity.BLOQUED
    StateDto.CLOSED   -> StateEntity.CLOSED
}

fun StateEntity.toDto(): StateDto = when (this) {
    StateEntity.ACTIVE   -> StateDto.ACTIVE
    StateEntity.INACTIVE -> StateDto.INACTIVE
    StateEntity.BLOQUED  -> StateDto.BLOQUED
    StateEntity.CLOSED   -> StateDto.CLOSED
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

