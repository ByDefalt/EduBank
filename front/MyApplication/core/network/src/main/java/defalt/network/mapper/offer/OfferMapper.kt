package defalt.network.mapper.offer

import defalt.domain.entity.offer.Offer as OfferEntity
import defalt.network.api.offer.model.Offer as OfferDto

import defalt.domain.entity.offer.OffersIdPutRequest as OffersIdPutRequestEntity
import defalt.network.api.offer.model.OffersIdPutRequest as OffersIdPutRequestDto

import defalt.domain.entity.offer.OffersIdStatePatchRequest as OffersIdStatePatchRequestEntity
import defalt.network.api.offer.model.OffersIdStatePatchRequest as OffersIdStatePatchRequestDto

import defalt.domain.entity.offer.OffersPostRequest as OffersPostRequestEntity
import defalt.network.api.offer.model.OffersPostRequest as OffersPostRequestDto

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
    OfferDto.State.ACTIVE   -> OfferEntity.State.ACTIVE
    OfferDto.State.INACTIVE -> OfferEntity.State.INACTIVE
    OfferDto.State.EXPIRED  -> OfferEntity.State.EXPIRED
}

fun OfferEntity.State.toDto(): OfferDto.State = when (this) {
    OfferEntity.State.ACTIVE   -> OfferDto.State.ACTIVE
    OfferEntity.State.INACTIVE -> OfferDto.State.INACTIVE
    OfferEntity.State.EXPIRED  -> OfferDto.State.EXPIRED
}

// ── OffersPostRequest ─────────────────────────────────────────────────────────

fun OffersPostRequestEntity.toDto(): OffersPostRequestDto = OffersPostRequestDto(
    title = this.title,
    description = this.description,
    state = this.state.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OffersPostRequestDto.toEntity(): OffersPostRequestEntity = OffersPostRequestEntity(
    title = this.title,
    description = this.description,
    state = this.state.toEntity(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OffersPostRequestDto.State.toEntity(): OffersPostRequestEntity.State = when (this) {
    OffersPostRequestDto.State.ACTIVE   -> OffersPostRequestEntity.State.ACTIVE
    OffersPostRequestDto.State.INACTIVE -> OffersPostRequestEntity.State.INACTIVE
    OffersPostRequestDto.State.EXPIRED  -> OffersPostRequestEntity.State.EXPIRED
}

fun OffersPostRequestEntity.State.toDto(): OffersPostRequestDto.State = when (this) {
    OffersPostRequestEntity.State.ACTIVE   -> OffersPostRequestDto.State.ACTIVE
    OffersPostRequestEntity.State.INACTIVE -> OffersPostRequestDto.State.INACTIVE
    OffersPostRequestEntity.State.EXPIRED  -> OffersPostRequestDto.State.EXPIRED
}

// ── OffersIdPutRequest ────────────────────────────────────────────────────────

fun OffersIdPutRequestEntity.toDto(): OffersIdPutRequestDto = OffersIdPutRequestDto(
    picturePath = this.picturePath,
    title = this.title,
    description = this.description,
    state = this.state?.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
)

fun OffersIdPutRequestDto.toEntity(): OffersIdPutRequestEntity = OffersIdPutRequestEntity(
    picturePath = this.picturePath,
    title = this.title,
    description = this.description,
    state = this.state?.toEntity(),
    startDate = this.startDate,
    endDate = this.endDate,
)

fun OffersIdPutRequestDto.State.toEntity(): OffersIdPutRequestEntity.State = when (this) {
    OffersIdPutRequestDto.State.ACTIVE   -> OffersIdPutRequestEntity.State.ACTIVE
    OffersIdPutRequestDto.State.INACTIVE -> OffersIdPutRequestEntity.State.INACTIVE
    OffersIdPutRequestDto.State.EXPIRED  -> OffersIdPutRequestEntity.State.EXPIRED
}

fun OffersIdPutRequestEntity.State.toDto(): OffersIdPutRequestDto.State = when (this) {
    OffersIdPutRequestEntity.State.ACTIVE   -> OffersIdPutRequestDto.State.ACTIVE
    OffersIdPutRequestEntity.State.INACTIVE -> OffersIdPutRequestDto.State.INACTIVE
    OffersIdPutRequestEntity.State.EXPIRED  -> OffersIdPutRequestDto.State.EXPIRED
}

// ── OffersIdStatePatchRequest ─────────────────────────────────────────────────

fun OffersIdStatePatchRequestEntity.toDto(): OffersIdStatePatchRequestDto = OffersIdStatePatchRequestDto(
    state = this.state.toDto(),
)

fun OffersIdStatePatchRequestDto.toEntity(): OffersIdStatePatchRequestEntity = OffersIdStatePatchRequestEntity(
    state = this.state.toEntity(),
)

fun OffersIdStatePatchRequestDto.State.toEntity(): OffersIdStatePatchRequestEntity.State = when (this) {
    OffersIdStatePatchRequestDto.State.ACTIVE   -> OffersIdStatePatchRequestEntity.State.ACTIVE
    OffersIdStatePatchRequestDto.State.INACTIVE -> OffersIdStatePatchRequestEntity.State.INACTIVE
    OffersIdStatePatchRequestDto.State.EXPIRED  -> OffersIdStatePatchRequestEntity.State.EXPIRED
}

fun OffersIdStatePatchRequestEntity.State.toDto(): OffersIdStatePatchRequestDto.State = when (this) {
    OffersIdStatePatchRequestEntity.State.ACTIVE   -> OffersIdStatePatchRequestDto.State.ACTIVE
    OffersIdStatePatchRequestEntity.State.INACTIVE -> OffersIdStatePatchRequestDto.State.INACTIVE
    OffersIdStatePatchRequestEntity.State.EXPIRED  -> OffersIdStatePatchRequestDto.State.EXPIRED
}

