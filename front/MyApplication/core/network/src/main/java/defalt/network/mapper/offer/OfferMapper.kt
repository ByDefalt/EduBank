package defalt.network.mapper.offer

import defalt.domain.entity.offer.Offer as OfferEntity
import defalt.network.api.offer.model.Offer as OfferDto
import defalt.domain.entity.offer.OfferInput as OfferInputEntity
import defalt.network.api.offer.model.OfferInput as OfferInputDto
import defalt.network.api.offer.model.Error as ErrorDto


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

@JvmName("offerListToDto")
fun List<OfferEntity>.toDto(): List<OfferDto> = this.map { it.toDto() }

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

fun OfferInputDto.toEntity(): OfferInputEntity = OfferInputEntity(
    title = this.title,
    description = this.description,
    state = this.state.toEntity(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OfferInputEntity.toDto(): OfferInputDto = OfferInputDto(
    title = this.title,
    description = this.description,
    state = this.state.toDto(),
    startDate = this.startDate,
    endDate = this.endDate,
    picturePath = this.picturePath,
)

fun OfferInputDto.State.toEntity(): OfferInputEntity.State = when (this) {
    OfferInputDto.State.ACTIVE -> OfferInputEntity.State.ACTIVE
    OfferInputDto.State.INACTIVE -> OfferInputEntity.State.INACTIVE
    OfferInputDto.State.EXPIRED -> OfferInputEntity.State.EXPIRED
}

fun OfferInputEntity.State.toDto(): OfferInputDto.State = when (this) {
    OfferInputEntity.State.ACTIVE -> OfferInputDto.State.ACTIVE
    OfferInputEntity.State.INACTIVE -> OfferInputDto.State.INACTIVE
    OfferInputEntity.State.EXPIRED -> OfferInputDto.State.EXPIRED
}

