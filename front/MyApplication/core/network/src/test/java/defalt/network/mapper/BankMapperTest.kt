package defalt.network.mapper.bank

import defalt.domain.entity.bank.BankAccount as BankAccountEntity
import defalt.domain.entity.bank.BankAccountCreateRequest as BankAccountCreateRequestEntity
import defalt.domain.entity.bank.BankAccountParameter as BankAccountParameterEntity
import defalt.domain.entity.bank.State as StateEntity
import defalt.domain.entity.bank.Type as TypeEntity
import defalt.network.api.bank.model.BankAccount as BankAccountDto
import defalt.network.api.bank.model.BankAccountDetail as BankAccountDetailDto
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto
import defalt.network.api.bank.model.State as StateDto
import defalt.network.api.bank.model.Type as TypeDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BankMapperTest {

    // ── State enum ──────────────────────────────────────────────────────────

    @Test fun `StateDto ACTIVE to entity`() = assertEquals(StateEntity.ACTIVE, StateDto.ACTIVE.toEntity())
    @Test fun `StateDto INACTIVE to entity`() = assertEquals(StateEntity.INACTIVE, StateDto.INACTIVE.toEntity())
    @Test fun `StateDto BLOQUED to entity`() = assertEquals(StateEntity.BLOQUED, StateDto.BLOQUED.toEntity())
    @Test fun `StateDto CLOSED to entity`() = assertEquals(StateEntity.CLOSED, StateDto.CLOSED.toEntity())

    @Test fun `StateEntity ACTIVE to dto`() = assertEquals(StateDto.ACTIVE, StateEntity.ACTIVE.toDto())
    @Test fun `StateEntity INACTIVE to dto`() = assertEquals(StateDto.INACTIVE, StateEntity.INACTIVE.toDto())
    @Test fun `StateEntity BLOQUED to dto`() = assertEquals(StateDto.BLOQUED, StateEntity.BLOQUED.toDto())
    @Test fun `StateEntity CLOSED to dto`() = assertEquals(StateDto.CLOSED, StateEntity.CLOSED.toDto())

    @Test fun `roundtrip State entity-dto-entity`() {
        StateEntity.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    // ── Type ────────────────────────────────────────────────────────────────

    @Test fun `TypeDto toEntity maps fields`() {
        val dto = TypeDto(id = 1, name = "CHEQUES")
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("CHEQUES", entity.name)
    }

    @Test fun `TypeEntity toDto maps fields`() {
        val entity = TypeEntity(id = 2, name = "EPARGNE")
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals("EPARGNE", dto.name)
    }

    @Test fun `roundtrip Type dto-entity-dto`() {
        val dto = TypeDto(id = 3, name = "PRO")
        assertEquals(dto, dto.toEntity().toDto())
    }

    // ── BankAccount ──────────────────────────────────────────────────────────

    @Test fun `BankAccountDto toEntity maps all fields`() {
        val dto = BankAccountDto(id = "bank-001", parameterId = 1, typeId = 2, sold = 500.0, iban = "FR76...")
        val entity = dto.toEntity()
        assertEquals("bank-001", entity.id)
        assertEquals(1, entity.parameterId)
        assertEquals(2, entity.typeId)
        assertEquals("FR76...", entity.iban)
    }

    @Test fun `BankAccountEntity toDto maps all fields`() {
        val entity = BankAccountEntity(id = "bank-001", parameterId = 1, typeId = 2, sold = 500.0, iban = "FR76...")
        val dto = entity.toDto()
        assertEquals("bank-001", dto.id)
    }

    @Test fun `roundtrip BankAccount dto-entity-dto`() {
        val dto = BankAccountDto(id = "bank-001", parameterId = 1, typeId = 2, sold = 1000.0, iban = "FR76...1")
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List BankAccountDto toEntity`() {
        val list = listOf(
            BankAccountDto(id = "bank-001", parameterId = 1, typeId = 1, sold = 100.0, iban = "FR76...1"),
            BankAccountDto(id = "bank-002", parameterId = 2, typeId = 2, sold = 200.0, iban = "FR76...2"),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals("bank-002", entities[1].id)
    }

    // ── BankAccountCreateRequest ─────────────────────────────────────────────

    @Test fun `BankAccountCreateRequestEntity toDto maps all fields`() {
        val entity = BankAccountCreateRequestEntity(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = StateEntity.ACTIVE)
        val dto = entity.toDto()
        assertEquals(1, dto.typeId)
        assertEquals("FR76...", dto.iban)
        assertEquals(StateDto.ACTIVE, dto.state)
    }

    @Test fun `BankAccountCreateRequestEntity toDto with null state`() {
        val entity = BankAccountCreateRequestEntity(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 0.0, state = null)
        assertNull(entity.toDto().state)
    }

    @Test fun `roundtrip BankAccountCreateRequest entity-dto-entity`() {
        val entity = BankAccountCreateRequestEntity(typeId = 2, iban = "FR76...2", sold = 100.0, overdraftLimit = 200.0, state = StateEntity.INACTIVE)
        assertEquals(entity, entity.toDto().toEntity())
    }

    // ── BankAccountParameter ─────────────────────────────────────────────────

    @Test fun `BankAccountParameterDto toEntity maps fields`() {
        val dto = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = StateDto.ACTIVE)
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals(StateEntity.ACTIVE, entity.state)
    }

    @Test fun `BankAccountParameterDto toEntity with null state`() {
        val dto = BankAccountParameterDto(id = 1, overdraftLimit = 0.0, state = null)
        assertNull(dto.toEntity().state)
    }

    @Test fun `BankAccountParameterEntity toDto maps fields`() {
        val entity = BankAccountParameterEntity(id = 2, overdraftLimit = 200.0, state = StateEntity.BLOQUED)
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals(StateDto.BLOQUED, dto.state)
    }

    @Test fun `roundtrip BankAccountParameter dto-entity-dto`() {
        val dto = BankAccountParameterDto(id = 3, overdraftLimit = 100.0, state = StateDto.CLOSED)
        assertEquals(dto, dto.toEntity().toDto())
    }

    // ── BankAccountDetail ─────────────────────────────────────────────────────

    @Test fun `BankAccountDetailDto toEntity maps all fields`() {
        val dto = BankAccountDetailDto(
            id = "bank-001",
            parameter = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = StateDto.ACTIVE),
            type = TypeDto(id = 1, name = "CHEQUES"),
            sold = 1000.0,
            iban = "FR76...",
        )
        val entity = dto.toEntity()
        assertEquals("bank-001", entity.id)
        assertEquals("CHEQUES", entity.type?.name)
        assertEquals("FR76...", entity.iban)
    }

    @Test fun `BankAccountDetailDto toEntity with null parameter and type`() {
        val dto = BankAccountDetailDto(id = "bank-001", parameter = null, type = null, sold = 0.0, iban = "FR76...")
        val entity = dto.toEntity()
        assertNull(entity.parameter)
        assertNull(entity.type)
    }

    @Test fun `roundtrip BankAccountDetail dto-entity-dto`() {
        val dto = BankAccountDetailDto(
            id = "bank-001",
            parameter = BankAccountParameterDto(id = 1, overdraftLimit = 200.0, state = StateDto.INACTIVE),
            type = TypeDto(id = 2, name = "EPARGNE"),
            sold = 500.0,
            iban = "FR76...1",
        )
        assertEquals(dto, dto.toEntity().toDto())
    }
}
