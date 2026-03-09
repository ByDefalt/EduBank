package defalt.network.mapper.bank

import defalt.domain.entity.bank.BankAccount as BankAccountEntity
import defalt.domain.entity.bank.BankAccountCreateRequest as BankAccountCreateRequestEntity
import defalt.domain.entity.bank.BankAccountDetail as BankAccountDetailEntity
import defalt.domain.entity.bank.BankAccountParameter as BankAccountParameterEntity
import defalt.domain.entity.bank.State as StateEntity
import defalt.domain.entity.bank.Type as TypeEntity
import defalt.network.api.bank.model.BankAccount as BankAccountDto
import defalt.network.api.bank.model.BankAccountDetails as BankAccountDetailsDto
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto
import defalt.network.api.bank.model.BankAccountsPostRequest as BankAccountsPostRequestDto
import defalt.network.api.bank.model.ParametersIdPutRequest as ParametersIdPutRequestDto
import defalt.network.api.bank.model.Type as TypeDto

// ── BankAccount ───────────────────────────────────────────────────────────────

fun BankAccountDto.toEntity(): BankAccountEntity = BankAccountEntity(
    id = this.id.toString(),
    parameterId = this.parameterId,
    typeId = this.typeId,
    sold = this.sold,
    iban = this.iban,
)

fun BankAccountEntity.toDto(): BankAccountDto = BankAccountDto(
    id = this.id?.toIntOrNull() ?: 0,
    parameterId = this.parameterId ?: 0,
    typeId = this.typeId ?: 0,
    sold = this.sold ?: 0.0,
    iban = this.iban ?: "",
)

@JvmName("bankAccountListToEntity")
fun List<BankAccountDto>.toEntity(): List<BankAccountEntity> = this.map { it.toEntity() }

// ── BankAccountDetails → BankAccountDetail ───────────────────────────────────

fun BankAccountDetailsDto.toEntity(): BankAccountDetailEntity = BankAccountDetailEntity(
    id = this.id.toString(),
    parameter = this.parameter?.toEntity(),
    type = this.type?.toEntity(),
    sold = this.sold,
    iban = this.iban,
)

fun BankAccountDetailEntity.toDto(): BankAccountDetailsDto = BankAccountDetailsDto(
    id = this.id?.toIntOrNull() ?: 0,
    parameterId = this.parameter?.id ?: 0,
    typeId = this.type?.id ?: 0,
    sold = this.sold ?: 0.0,
    iban = this.iban ?: "",
    parameter = this.parameter?.toDto(),
    type = this.type?.toDto(),
)

@JvmName("bankAccountDetailsListToEntity")
fun List<BankAccountDetailsDto>.toEntity(): List<BankAccountDetailEntity> = this.map { it.toEntity() }

@JvmName("bankAccountDetailsListToBankAccountEntity")
fun List<BankAccountDetailsDto>.toBankAccountEntity(): List<BankAccountEntity> = this.map {
    BankAccountEntity(
        id = it.id.toString(),
        parameterId = it.parameterId,
        typeId = it.typeId,
        sold = it.sold,
        iban = it.iban,
    )
}

// ── BankAccountCreateRequest ──────────────────────────────────────────────────

fun BankAccountCreateRequestEntity.toDto(): BankAccountsPostRequestDto = BankAccountsPostRequestDto(
    parameterId = 0, // sera géré côté serveur
    typeId = this.typeId,
    sold = this.sold,
    iban = this.iban,
    accountId = 0, // fourni séparément lors de l'appel
)

// ── BankAccountParameter ──────────────────────────────────────────────────────

fun BankAccountParameterDto.toEntity(): BankAccountParameterEntity = BankAccountParameterEntity(
    id = this.id,
    overdraftLimit = this.overdraftLimit,
    state = this.state.toEntity(),
)

fun BankAccountParameterEntity.toDto(): BankAccountParameterDto = BankAccountParameterDto(
    id = this.id ?: 0,
    overdraftLimit = this.overdraftLimit ?: 0.0,
    state = this.state?.toDto() ?: BankAccountParameterDto.State.ACTIVE,
)

fun BankAccountParameterEntity.toParametersIdPutRequestDto(): ParametersIdPutRequestDto = ParametersIdPutRequestDto(
    overdraftLimit = this.overdraftLimit,
    state = this.state?.toParameterState(),
)

// ── BankAccountParameter.State ↔ StateEntity ─────────────────────────────────

fun BankAccountParameterDto.State.toEntity(): StateEntity = when (this) {
    BankAccountParameterDto.State.ACTIVE -> StateEntity.ACTIVE
    BankAccountParameterDto.State.INACTIVE -> StateEntity.INACTIVE
}

fun StateEntity.toDto(): BankAccountParameterDto.State = when (this) {
    StateEntity.ACTIVE -> BankAccountParameterDto.State.ACTIVE
    StateEntity.INACTIVE -> BankAccountParameterDto.State.INACTIVE
    StateEntity.BLOQUED -> BankAccountParameterDto.State.INACTIVE
    StateEntity.CLOSED -> BankAccountParameterDto.State.INACTIVE
}

fun StateEntity.toParameterState(): ParametersIdPutRequestDto.State = when (this) {
    StateEntity.ACTIVE -> ParametersIdPutRequestDto.State.ACTIVE
    StateEntity.INACTIVE -> ParametersIdPutRequestDto.State.INACTIVE
    StateEntity.BLOQUED -> ParametersIdPutRequestDto.State.INACTIVE
    StateEntity.CLOSED -> ParametersIdPutRequestDto.State.INACTIVE
}

// ── Type ──────────────────────────────────────────────────────────────────────

fun TypeDto.toEntity(): TypeEntity = TypeEntity(
    id = this.id,
    name = this.name,
)

fun TypeEntity.toDto(): TypeDto = TypeDto(
    id = this.id ?: 0,
    name = this.name ?: "",
)
