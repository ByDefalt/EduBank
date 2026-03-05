package defalt.network.mapper.operation

import defalt.domain.entity.operation.Beneficiary as BeneficiaryEntity
import defalt.network.api.operation.model.Beneficiary as BeneficiaryDto

import defalt.domain.entity.operation.Operation as OperationEntity
import defalt.network.api.operation.model.Operation as OperationDto

import defalt.domain.entity.operation.OperationState as OperationStateEntity
import defalt.network.api.operation.model.OperationState as OperationStateDto

// ── Beneficiary ───────────────────────────────────────────────────────────────

fun BeneficiaryDto.toEntity(): BeneficiaryEntity = BeneficiaryEntity(
    accountSourceId = this.accountSourceId,
    ibanTarget = this.ibanTarget,
    name = this.name,
    id = this.id,
)

fun BeneficiaryEntity.toDto(): BeneficiaryDto = BeneficiaryDto(
    accountSourceId = this.accountSourceId,
    ibanTarget = this.ibanTarget,
    name = this.name,
    id = this.id,
)

@JvmName("beneficiaryListToEntity")
fun List<BeneficiaryDto>.toEntity(): List<BeneficiaryEntity> = this.map { it.toEntity() }

// ── Operation ─────────────────────────────────────────────────────────────────

fun OperationDto.toEntity(): OperationEntity = OperationEntity(
    id = this.id,
    accountSourceId = this.accountSourceId,
    label = this.label,
    state = this.state.toEntity(),
    ibanTarget = this.ibanTarget,
    amount = this.amount,
    date = this.date,
)

fun OperationEntity.toDto(): OperationDto = OperationDto(
    id = this.id,
    accountSourceId = this.accountSourceId,
    label = this.label,
    state = this.state.toDto(),
    ibanTarget = this.ibanTarget,
    amount = this.amount,
    date = this.date,
)

fun List<OperationDto>.toEntity(): List<OperationEntity> = this.map { it.toEntity() }

// ── OperationState (enum) ─────────────────────────────────────────────────────

fun OperationStateDto.toEntity(): OperationStateEntity = when (this) {
    OperationStateDto.PENDING   -> OperationStateEntity.PENDING
    OperationStateDto.COMPLETED -> OperationStateEntity.COMPLETED
    OperationStateDto.FAILED    -> OperationStateEntity.FAILED
    OperationStateDto.CANCELLED -> OperationStateEntity.CANCELLED
}

fun OperationStateEntity.toDto(): OperationStateDto = when (this) {
    OperationStateEntity.PENDING   -> OperationStateDto.PENDING
    OperationStateEntity.COMPLETED -> OperationStateDto.COMPLETED
    OperationStateEntity.FAILED    -> OperationStateDto.FAILED
    OperationStateEntity.CANCELLED -> OperationStateDto.CANCELLED
}

