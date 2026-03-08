package defalt.network.mapper.offer

import defalt.domain.entity.offer.Offer as OfferEntity
import defalt.domain.entity.offer.OffersIdPutRequest as OffersIdPutRequestEntity
import defalt.domain.entity.offer.OffersIdStatePatchRequest as OffersIdStatePatchRequestEntity
import defalt.domain.entity.offer.OffersPostRequest as OffersPostRequestEntity
import defalt.network.api.offer.model.Offer as OfferDto
import defalt.network.api.offer.model.OffersIdPutRequest as OffersIdPutRequestDto
import defalt.network.api.offer.model.OffersIdStatePatchRequest as OffersIdStatePatchRequestDto
import defalt.network.api.offer.model.OffersPostRequest as OffersPostRequestDto
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OfferMapperTest {

    private val today: LocalDate = LocalDate.now()
    private val nextMonth: LocalDate = today.plusMonths(1)

    // ── Offer.State enum ─────────────────────────────────────────────────────

    @Test fun `OfferDto State ACTIVE to entity`() = assertEquals(OfferEntity.State.ACTIVE, OfferDto.State.ACTIVE.toEntity())
    @Test fun `OfferDto State INACTIVE to entity`() = assertEquals(OfferEntity.State.INACTIVE, OfferDto.State.INACTIVE.toEntity())
    @Test fun `OfferDto State EXPIRED to entity`() = assertEquals(OfferEntity.State.EXPIRED, OfferDto.State.EXPIRED.toEntity())

    @Test fun `OfferEntity State ACTIVE to dto`() = assertEquals(OfferDto.State.ACTIVE, OfferEntity.State.ACTIVE.toDto())
    @Test fun `OfferEntity State INACTIVE to dto`() = assertEquals(OfferDto.State.INACTIVE, OfferEntity.State.INACTIVE.toDto())
    @Test fun `OfferEntity State EXPIRED to dto`() = assertEquals(OfferDto.State.EXPIRED, OfferEntity.State.EXPIRED.toDto())

    @Test fun `roundtrip Offer State entity-dto-entity`() {
        OfferEntity.State.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    // ── Offer ────────────────────────────────────────────────────────────────

    @Test fun `OfferDto toEntity maps all fields`() {
        val dto = OfferDto(id = 1, title = "Titre", description = "Desc",
            state = OfferDto.State.ACTIVE, startDate = today, endDate = nextMonth, picturePath = "/img.png")
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("Titre", entity.title)
        assertEquals("Desc", entity.description)
        assertEquals(OfferEntity.State.ACTIVE, entity.state)
        assertEquals(today, entity.startDate)
        assertEquals(nextMonth, entity.endDate)
        assertEquals("/img.png", entity.picturePath)
    }

    @Test fun `OfferDto toEntity with null picturePath`() {
        val dto = OfferDto(id = 1, title = "T", description = "D",
            state = OfferDto.State.INACTIVE, startDate = today, endDate = nextMonth, picturePath = null)
        assertNull(dto.toEntity().picturePath)
    }

    @Test fun `OfferEntity toDto maps all fields`() {
        val entity = OfferEntity(id = 2, title = "Test", description = "D",
            state = OfferEntity.State.EXPIRED, startDate = today, endDate = nextMonth)
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals(OfferDto.State.EXPIRED, dto.state)
    }

    @Test fun `roundtrip Offer dto-entity-dto`() {
        val dto = OfferDto(id = 1, title = "T", description = "D",
            state = OfferDto.State.ACTIVE, startDate = today, endDate = nextMonth, picturePath = null)
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List OfferDto toEntity`() {
        val list = listOf(
            OfferDto(id = 1, title = "A", description = "D", state = OfferDto.State.ACTIVE, startDate = today, endDate = nextMonth),
            OfferDto(id = 2, title = "B", description = "D", state = OfferDto.State.INACTIVE, startDate = today, endDate = nextMonth),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals(1, entities[0].id)
        assertEquals(2, entities[1].id)
    }

    // ── OffersPostRequest ────────────────────────────────────────────────────

    @Test fun `OffersPostRequestEntity toDto maps all fields`() {
        val entity = OffersPostRequestEntity(
            title = "Titre", description = "Desc",
            state = OffersPostRequestEntity.State.ACTIVE,
            startDate = today, endDate = nextMonth, picturePath = null,
        )
        val dto = entity.toDto()
        assertEquals("Titre", dto.title)
        assertEquals(OffersPostRequestDto.State.ACTIVE, dto.state)
        assertEquals(today, dto.startDate)
    }

    @Test fun `OffersPostRequest State roundtrip entity-dto-entity`() {
        OffersPostRequestEntity.State.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    @Test fun `roundtrip OffersPostRequest entity-dto-entity`() {
        val entity = OffersPostRequestEntity(
            title = "T", description = "D",
            state = OffersPostRequestEntity.State.INACTIVE,
            startDate = today, endDate = nextMonth, picturePath = null,
        )
        assertEquals(entity, entity.toDto().toEntity())
    }

    // ── OffersIdPutRequest ───────────────────────────────────────────────────

    @Test fun `OffersIdPutRequestEntity toDto maps all fields`() {
        val entity = OffersIdPutRequestEntity(
            title = "Nouveau", description = "Desc",
            state = OffersIdPutRequestEntity.State.ACTIVE,
            startDate = today, endDate = nextMonth, picturePath = null,
        )
        val dto = entity.toDto()
        assertEquals("Nouveau", dto.title)
        assertEquals(OffersIdPutRequestDto.State.ACTIVE, dto.state)
    }

    @Test fun `OffersIdPutRequestEntity toDto with null state`() {
        val entity = OffersIdPutRequestEntity(title = "T", state = null)
        assertNull(entity.toDto().state)
    }

    @Test fun `OffersIdPutRequest State roundtrip entity-dto-entity`() {
        OffersIdPutRequestEntity.State.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    @Test fun `roundtrip OffersIdPutRequest entity-dto-entity`() {
        val entity = OffersIdPutRequestEntity(
            title = "T", description = "D",
            state = OffersIdPutRequestEntity.State.EXPIRED,
            startDate = today, endDate = nextMonth,
        )
        assertEquals(entity, entity.toDto().toEntity())
    }

    // ── OffersIdStatePatchRequest ────────────────────────────────────────────
    // OffersIdStatePatchRequest.State est son propre enum, distinct de OffersIdPutRequest.State

    @Test fun `OffersIdStatePatchRequestEntity ACTIVE toDto`() {
        val entity = OffersIdStatePatchRequestEntity(state = OffersIdStatePatchRequestEntity.State.ACTIVE)
        assertEquals(OffersIdStatePatchRequestDto.State.ACTIVE, entity.toDto().state)
    }

    @Test fun `OffersIdStatePatchRequestEntity INACTIVE toDto`() {
        val entity = OffersIdStatePatchRequestEntity(state = OffersIdStatePatchRequestEntity.State.INACTIVE)
        assertEquals(OffersIdStatePatchRequestDto.State.INACTIVE, entity.toDto().state)
    }

    @Test fun `OffersIdStatePatchRequestEntity EXPIRED toDto`() {
        val entity = OffersIdStatePatchRequestEntity(state = OffersIdStatePatchRequestEntity.State.EXPIRED)
        assertEquals(OffersIdStatePatchRequestDto.State.EXPIRED, entity.toDto().state)
    }
}
