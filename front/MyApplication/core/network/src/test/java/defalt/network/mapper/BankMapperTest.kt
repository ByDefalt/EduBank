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
import defalt.network.api.bank.model.Type as TypeDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BankMapperTest {

    // ── BankAccountParameter.State enum ────────────────────────────────────
    // La State est maintenant une enum imbriquée dans BankAccountParameter (ACTIVE/INACTIVE)
    // StateEntity.BLOQUED et CLOSED sont mappés vers INACTIVE côté DTO

    @Test fun `BankAccountParameterState ACTIVE to entity`() =
        assertEquals(StateEntity.ACTIVE, BankAccountParameterDto.State.ACTIVE.toEntity())

    @Test fun `BankAccountParameterState INACTIVE to entity`() =
        assertEquals(StateEntity.INACTIVE, BankAccountParameterDto.State.INACTIVE.toEntity())

    @Test fun `StateEntity ACTIVE to BankAccountParameterState dto`() =
        assertEquals(BankAccountParameterDto.State.ACTIVE, StateEntity.ACTIVE.toDto())

    @Test fun `StateEntity INACTIVE to BankAccountParameterState dto`() =
        assertEquals(BankAccountParameterDto.State.INACTIVE, StateEntity.INACTIVE.toDto())

    @Test fun `StateEntity BLOQUED to BankAccountParameterState dto maps to INACTIVE`() =
        assertEquals(BankAccountParameterDto.State.INACTIVE, StateEntity.BLOQUED.toDto())

    @Test fun `StateEntity CLOSED to BankAccountParameterState dto maps to INACTIVE`() =
        assertEquals(BankAccountParameterDto.State.INACTIVE, StateEntity.CLOSED.toDto())

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
    // BankAccount.id est maintenant un Int dans le DTO

    @Test fun `BankAccountDto toEntity maps all fields`() {
        val dto = BankAccountDto(id = 1, parameterId = 1, typeId = 2, sold = 500.0, iban = "FR76...")
        val entity = dto.toEntity()
        assertEquals("1", entity.id)
        assertEquals(1, entity.parameterId)
        assertEquals(2, entity.typeId)
        assertEquals(500.0, entity.sold!!, 0.0)
        assertEquals("FR76...", entity.iban)
    }

    @Test fun `BankAccountEntity toDto maps id to Int`() {
        val entity = BankAccountEntity(id = "42", parameterId = 1, typeId = 2, sold = 500.0, iban = "FR76...")
        val dto = entity.toDto()
        assertEquals(42, dto.id)
    }

    @Test fun `List BankAccountDto toEntity`() {
        val list = listOf(
            BankAccountDto(id = 1, parameterId = 1, typeId = 1, sold = 100.0, iban = "FR76...1"),
            BankAccountDto(id = 2, parameterId = 2, typeId = 2, sold = 200.0, iban = "FR76...2"),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals("2", entities[1].id)
    }

    // ── BankAccountCreateRequest ─────────────────────────────────────────────
    // La conversion est sens unique Entity → BankAccountsPostRequest (pas de roundtrip)

    @Test fun `BankAccountCreateRequestEntity toDto maps typeId iban sold`() {
        val entity = BankAccountCreateRequestEntity(typeId = 1, iban = "FR76...", sold = 100.0, overdraftLimit = 500.0, state = StateEntity.ACTIVE)
        val dto = entity.toDto()
        assertEquals(1, dto.typeId)
        assertEquals("FR76...", dto.iban)
        assertEquals(100.0, dto.sold, 0.0)
    }

    @Test fun `BankAccountCreateRequestEntity toDto with null state`() {
        val entity = BankAccountCreateRequestEntity(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 0.0, state = null)
        // toDto() retourne BankAccountsPostRequest qui n'a pas de champ state — pas d'assertion sur state
        val dto = entity.toDto()
        assertEquals(1, dto.typeId)
    }

    // ── BankAccountParameter ─────────────────────────────────────────────────

    @Test fun `BankAccountParameterDto toEntity maps fields`() {
        val dto = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = BankAccountParameterDto.State.ACTIVE)
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals(500.0, entity.overdraftLimit!!, 0.0)
        assertEquals(StateEntity.ACTIVE, entity.state)
    }

    @Test fun `BankAccountParameterEntity toDto maps fields`() {
        val entity = BankAccountParameterEntity(id = 2, overdraftLimit = 200.0, state = StateEntity.ACTIVE)
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals(200.0, dto.overdraftLimit, 0.0)
        assertEquals(BankAccountParameterDto.State.ACTIVE, dto.state)
    }

    @Test fun `BankAccountParameterEntity toDto BLOQUED maps to INACTIVE`() {
        val entity = BankAccountParameterEntity(id = 3, overdraftLimit = 0.0, state = StateEntity.BLOQUED)
        assertEquals(BankAccountParameterDto.State.INACTIVE, entity.toDto().state)
    }

    @Test fun `roundtrip BankAccountParameter dto ACTIVE entity dto`() {
        val dto = BankAccountParameterDto(id = 3, overdraftLimit = 100.0, state = BankAccountParameterDto.State.ACTIVE)
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `roundtrip BankAccountParameter dto INACTIVE entity dto`() {
        val dto = BankAccountParameterDto(id = 4, overdraftLimit = 50.0, state = BankAccountParameterDto.State.INACTIVE)
        assertEquals(dto, dto.toEntity().toDto())
    }

    // ── BankAccountDetails → BankAccountDetail ────────────────────────────────

    @Test fun `BankAccountDetailsDto toEntity maps all fields`() {
        val dto = BankAccountDetailsDto(
            id = 1,
            parameterId = 1,
            typeId = 1,
            sold = 1000.0,
            iban = "FR76...",
            parameter = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = BankAccountParameterDto.State.ACTIVE),
            type = TypeDto(id = 1, name = "CHEQUES"),
        )
        val entity = dto.toEntity()
        assertEquals("1", entity.id)
        assertEquals(1000.0, entity.sold!!, 0.0)
        assertEquals("FR76...", entity.iban)
        assertEquals("CHEQUES", entity.type?.name)
        assertEquals(StateEntity.ACTIVE, entity.parameter?.state)
    }

    @Test fun `BankAccountDetailsDto toEntity with null parameter and type`() {
        val dto = BankAccountDetailsDto(id = 1, parameterId = 0, typeId = 0, sold = 0.0, iban = "FR76...", parameter = null, type = null)
        val entity = dto.toEntity()
        assertNull(entity.parameter)
        assertNull(entity.type)
    }

    @Test fun `List BankAccountDetails toEntity`() {
        val list = listOf(
            BankAccountDetailsDto(id = 1, parameterId = 1, typeId = 1, sold = 100.0, iban = "FR1",
                parameter = BankAccountParameterDto(1, 100.0, BankAccountParameterDto.State.ACTIVE),
                type = TypeDto(1, "A")),
            BankAccountDetailsDto(id = 2, parameterId = 2, typeId = 2, sold = 200.0, iban = "FR2",
                parameter = BankAccountParameterDto(2, 200.0, BankAccountParameterDto.State.INACTIVE),
                type = TypeDto(2, "B")),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals("2", entities[1].id)
    }

    @Test fun `List BankAccountDetails toBankAccountEntity`() {
        val list = listOf(
            BankAccountDetailsDto(id = 10, parameterId = 1, typeId = 1, sold = 500.0, iban = "FR10",
                parameter = null, type = null),
        )
        val entities = list.toBankAccountEntity()
        assertEquals(1, entities.size)
        assertEquals("10", entities[0].id)
        assertEquals(500.0, entities[0].sold!!, 0.0)
    }

    @Test fun `BankAccountDetailEntity toDto maps all fields`() {
        val entity = BankAccountDetailEntity(
            id = "5",
            parameter = BankAccountParameterEntity(id = 1, overdraftLimit = 300.0, state = StateEntity.ACTIVE),
            type = TypeEntity(id = 2, name = "EPARGNE"),
            sold = 750.0,
            iban = "FR76...5",
        )
        val dto = entity.toDto()
        assertEquals(5, dto.id)
        assertEquals(750.0, dto.sold, 0.0)
        assertEquals("FR76...5", dto.iban)
        assertEquals("EPARGNE", dto.type?.name)
    }
}
