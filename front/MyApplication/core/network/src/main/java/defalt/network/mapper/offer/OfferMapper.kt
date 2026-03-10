package defalt.network.mapper.offer

import defalt.domain.entity.offer.Offer as OfferEntity
import defalt.domain.entity.offer.OffersIdPutRequest as OffersIdPutRequestEntity
import defalt.domain.entity.offer.OffersIdStatePatchRequest as OffersIdStatePatchRequestEntity
import defalt.domain.entity.offer.OffersPostRequest as OffersPostRequestEntity
import defalt.network.api.offer.model.Offer as OfferDto
import defalt.network.api.offer.model.OfferInput as OfferInputDto

// ── Offer ─────────────────────────────────────────────────────────────────────

fun OfferDto.toEntity(): OfferEntity = OfferEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    state = this.state.toEntity(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OfferEntity.toDto(): OfferDto = OfferDto(
    id = this.id,
    title = this.title,
    description = this.description,
    state = this.state.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

@JvmName("offerListToEntity")
fun List<OfferDto>.toEntity(): List<OfferEntity> = this.map { it.toEntity() }

// ── Offer.State (enum) ────────────────────────────────────────────────────────

fun OfferDto.State.toEntity(): OfferEntity.State = when (this) {
    OfferDto.State.ACTIVE -> OfferEntity.State.ACTIVE
    OfferDto.State.INACTIVE -> OfferEntity.State.INACTIVE
    OfferDto.State.EXPIRED -> OfferEntity.State.EXPIRED
}

fun OfferEntity.State.toDto(): OfferDto.State = when (this) {
    OfferEntity.State.ACTIVE -> OfferDto.State.ACTIVE
    OfferEntity.State.INACTIVE -> OfferDto.State.INACTIVE
    OfferEntity.State.EXPIRED -> OfferDto.State.EXPIRED
}

// ── OfferInput.State (enum) ───────────────────────────────────────────────────

fun OfferInputDto.State.toEntity(): OfferEntity.State = when (this) {
    OfferInputDto.State.ACTIVE -> OfferEntity.State.ACTIVE
    OfferInputDto.State.INACTIVE -> OfferEntity.State.INACTIVE
    OfferInputDto.State.EXPIRED -> OfferEntity.State.EXPIRED
}

fun OfferEntity.State.toOfferInputState(): OfferInputDto.State = when (this) {
    OfferEntity.State.ACTIVE -> OfferInputDto.State.ACTIVE
    OfferEntity.State.INACTIVE -> OfferInputDto.State.INACTIVE
    OfferEntity.State.EXPIRED -> OfferInputDto.State.EXPIRED
}

// ── OffersPostRequest → OfferInput ────────────────────────────────────────────

fun OffersPostRequestEntity.toDto(): OfferInputDto = OfferInputDto(
    title = this.title,
    description = this.description,
    state = this.state.toOfferInputState(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OffersPostRequestEntity.State.toOfferInputState(): OfferInputDto.State = when (this) {
    OffersPostRequestEntity.State.ACTIVE -> OfferInputDto.State.ACTIVE
    OffersPostRequestEntity.State.INACTIVE -> OfferInputDto.State.INACTIVE
    OffersPostRequestEntity.State.EXPIRED -> OfferInputDto.State.EXPIRED
}

// ── OffersIdPutRequest → OfferInput ───────────────────────────────────────────

fun OffersIdPutRequestEntity.toDto(): OfferInputDto = OfferInputDto(
    title = this.title ?: "",
    description = this.description ?: "",
    state = this.state?.toOfferInputState() ?: OfferInputDto.State.ACTIVE,
    startDate = this.startDate ?: java.time.LocalDate.now(),
    endDate = this.endDate ?: java.time.LocalDate.now(),
    picturePath = this.picturePath,
)

fun OffersIdPutRequestEntity.State.toOfferInputState(): OfferInputDto.State = when (this) {
    OffersIdPutRequestEntity.State.ACTIVE -> OfferInputDto.State.ACTIVE
    OffersIdPutRequestEntity.State.INACTIVE -> OfferInputDto.State.INACTIVE
    OffersIdPutRequestEntity.State.EXPIRED -> OfferInputDto.State.EXPIRED
}

// ── OffersIdStatePatchRequest → OfferInput (patch partiel via PUT) ────────────

fun OffersIdStatePatchRequestEntity.toOfferInputDto(existing: OfferEntity): OfferInputDto = OfferInputDto(
    title = existing.title,
    description = existing.description,
    state = this.state.toOfferInputState(),
    startDate = existing.startDate,
    endDate = existing.endDate,
    picturePath = existing.picturePath,
)

fun OffersIdStatePatchRequestEntity.State.toOfferInputState(): OfferInputDto.State = when (this) {
    OffersIdStatePatchRequestEntity.State.ACTIVE -> OfferInputDto.State.ACTIVE
    OffersIdStatePatchRequestEntity.State.INACTIVE -> OfferInputDto.State.INACTIVE
    OffersIdStatePatchRequestEntity.State.EXPIRED -> OfferInputDto.State.EXPIRED
}


