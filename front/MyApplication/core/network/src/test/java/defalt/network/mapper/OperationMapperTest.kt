package defalt.network.mapper.operation

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.OffsetDateTime
import defalt.domain.entity.operation.Beneficiary as BeneficiaryEntity
import defalt.domain.entity.operation.Operation as OperationEntity
import defalt.domain.entity.operation.OperationState as OperationStateEntity
import defalt.network.api.operation.model.Beneficiary as BeneficiaryDto
import defalt.network.api.operation.model.Operation as OperationDto
import defalt.network.api.operation.model.OperationState as OperationStateDto

class OperationMapperTest {

    private val now: OffsetDateTime = OffsetDateTime.now()

    // ── OperationState enum ──────────────────────────────────────────────────

    @Test fun `OperationStateDto PENDING to entity`() =
        assertEquals(OperationStateEntity.PENDING, OperationStateDto.PENDING.toEntity())

    @Test fun `OperationStateDto COMPLETED to entity`() =
        assertEquals(OperationStateEntity.COMPLETED, OperationStateDto.COMPLETED.toEntity())

    @Test fun `OperationStateDto FAILED to entity`() =
        assertEquals(OperationStateEntity.FAILED, OperationStateDto.FAILED.toEntity())

    @Test fun `OperationStateDto CANCELLED to entity`() =
        assertEquals(OperationStateEntity.CANCELLED, OperationStateDto.CANCELLED.toEntity())

    @Test fun `OperationStateEntity PENDING to dto`() =
        assertEquals(OperationStateDto.PENDING, OperationStateEntity.PENDING.toDto())

    @Test fun `OperationStateEntity COMPLETED to dto`() =
        assertEquals(OperationStateDto.COMPLETED, OperationStateEntity.COMPLETED.toDto())

    @Test fun `OperationStateEntity FAILED to dto`() =
        assertEquals(OperationStateDto.FAILED, OperationStateEntity.FAILED.toDto())

    @Test fun `OperationStateEntity CANCELLED to dto`() =
        assertEquals(OperationStateDto.CANCELLED, OperationStateEntity.CANCELLED.toDto())

    @Test fun `roundtrip OperationState entity-dto-entity`() {
        OperationStateEntity.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    // ── Beneficiary ──────────────────────────────────────────────────────────

    @Test fun `BeneficiaryDto toEntity maps all fields`() {
        val dto = BeneficiaryDto(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice")
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("acc-001", entity.accountSourceId)
        assertEquals("FR76...", entity.ibanTarget)
        assertEquals("Alice", entity.name)
    }

    @Test fun `BeneficiaryEntity toDto maps all fields`() {
        val entity = BeneficiaryEntity(id = 2, accountSourceId = "acc-002", ibanTarget = "FR76...2", name = "Bob")
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals("FR76...2", dto.ibanTarget)
        assertEquals("Bob", dto.name)
    }

    @Test fun `roundtrip Beneficiary dto-entity-dto`() {
        val dto = BeneficiaryDto(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice")
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List BeneficiaryDto toEntity`() {
        val list = listOf(
            BeneficiaryDto(id = 1, accountSourceId = "a", ibanTarget = "FR76...1", name = "Alice"),
            BeneficiaryDto(id = 2, accountSourceId = "a", ibanTarget = "FR76...2", name = "Bob"),
            BeneficiaryDto(id = 3, accountSourceId = "a", ibanTarget = "FR76...3", name = "Charlie"),
        )
        val entities = list.toEntity()
        assertEquals(3, entities.size)
        assertEquals("Bob", entities[1].name)
    }

    // ── Operation ────────────────────────────────────────────────────────────

    @Test fun `OperationDto toEntity maps all fields`() {
        val dto = OperationDto(
            id = 1,
            accountSourceId = "acc-001",
            label = "Virement",
            state = OperationStateDto.PENDING,
            ibanTarget = "FR76...",
            amount = 100.0,
            date = now,
        )
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("acc-001", entity.accountSourceId)
        assertEquals("Virement", entity.label)
        assertEquals(OperationStateEntity.PENDING, entity.state)
        assertEquals("FR76...", entity.ibanTarget)
        assertEquals(100.0, entity.amount, 0.0)
        assertEquals(now, entity.date)
    }

    @Test fun `OperationEntity toDto maps all fields`() {
        val entity = OperationEntity(
            id = 2,
            accountSourceId = "acc-002",
            label = "Prelevement",
            state = OperationStateEntity.COMPLETED,
            ibanTarget = "FR76...2",
            amount = 50.0,
            date = now,
        )
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals(OperationStateDto.COMPLETED, dto.state)
        assertEquals(50.0, dto.amount, 0.0)
    }

    @Test fun `roundtrip Operation dto-entity-dto`() {
        val dto = OperationDto(
            id = 3,
            accountSourceId = "acc-003",
            label = "Retrait",
            state = OperationStateDto.FAILED,
            ibanTarget = "FR76...3",
            amount = 200.0,
            date = now,
        )
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List OperationDto toEntity`() {
        val list = listOf(
            OperationDto(id = 1, accountSourceId = "a", label = "V1", state = OperationStateDto.PENDING, ibanTarget = "FR76...1", amount = 10.0, date = now),
            OperationDto(id = 2, accountSourceId = "a", label = "V2", state = OperationStateDto.COMPLETED, ibanTarget = "FR76...2", amount = 20.0, date = now),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals(OperationStateEntity.COMPLETED, entities[1].state)
    }

    @Test fun `roundtrip all OperationState values in Operation`() {
        OperationStateDto.entries.forEach { state ->
            val dto = OperationDto(
                id = 1,
                accountSourceId = "a",
                label = "L",
                state = state,
                ibanTarget = "FR76...",
                amount = 0.0,
                date = now,
            )
            assertEquals(state, dto.toEntity().toDto().state)
        }
    }
}
