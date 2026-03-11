package defalt.network.mapper.account

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import defalt.domain.entity.account.Account as AccountEntity
import defalt.domain.entity.account.AccountRegister as AccountRegisterEntity
import defalt.domain.entity.account.AccountStateEnum as AccountStateEnumEntity
import defalt.domain.entity.account.PersonalInformation as PersonalInformationEntity
import defalt.domain.entity.account.PersonalInformationRegister as PersonalInformationRegisterEntity
import defalt.domain.entity.account.Role as RoleEntity
import defalt.domain.entity.account.RoleEnum as RoleEnumEntity
import defalt.domain.entity.account.SignInRequest as SignInRequestEntity
import defalt.domain.entity.account.TokenRequest as TokenRequestEntity
import defalt.network.api.account.model.Account as AccountDto
import defalt.network.api.account.model.AccountStateEnum as AccountStateEnumDto
import defalt.network.api.account.model.PersonalInformation as PersonalInformationDto
import defalt.network.api.account.model.Role as RoleDto
import defalt.network.api.account.model.RoleEnum as RoleEnumDto
import defalt.network.api.account.model.TokenResponse as TokenResponseDto

class AccountMapperTest {

    // ── AccountStateEnum ────────────────────────────────────────────────────

    @Test fun `AccountStateEnumDto ACTIVE to entity`() =
        assertEquals(AccountStateEnumEntity.ACTIVE, AccountStateEnumDto.ACTIVE.toEntity())

    @Test fun `AccountStateEnumDto INACTIVE to entity`() =
        assertEquals(AccountStateEnumEntity.INACTIVE, AccountStateEnumDto.INACTIVE.toEntity())

    @Test fun `AccountStateEnumDto ENCLOSE to entity`() =
        assertEquals(AccountStateEnumEntity.ENCLOSE, AccountStateEnumDto.ENCLOSE.toEntity())

    @Test fun `AccountStateEnumEntity ACTIVE to dto`() =
        assertEquals(AccountStateEnumDto.ACTIVE, AccountStateEnumEntity.ACTIVE.toDto())

    @Test fun `AccountStateEnumEntity INACTIVE to dto`() =
        assertEquals(AccountStateEnumDto.INACTIVE, AccountStateEnumEntity.INACTIVE.toDto())

    @Test fun `AccountStateEnumEntity ENCLOSE to dto`() =
        assertEquals(AccountStateEnumDto.ENCLOSE, AccountStateEnumEntity.ENCLOSE.toDto())

    @Test fun `roundtrip AccountStateEnum entity-dto-entity`() {
        AccountStateEnumEntity.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    // ── RoleEnum ────────────────────────────────────────────────────────────

    @Test fun `RoleEnumDto ADMIN to entity`() =
        assertEquals(RoleEnumEntity.ADMIN, RoleEnumDto.ADMIN.toEntity())

    @Test fun `RoleEnumDto CUSTOMER to entity`() =
        assertEquals(RoleEnumEntity.CUSTOMER, RoleEnumDto.CUSTOMER.toEntity())

    @Test fun `RoleEnumEntity ADMIN to dto`() =
        assertEquals(RoleEnumDto.ADMIN, RoleEnumEntity.ADMIN.toDto())

    @Test fun `RoleEnumEntity CUSTOMER to dto`() =
        assertEquals(RoleEnumDto.CUSTOMER, RoleEnumEntity.CUSTOMER.toDto())

    @Test fun `roundtrip RoleEnum entity-dto-entity`() {
        RoleEnumEntity.entries.forEach { e -> assertEquals(e, e.toDto().toEntity()) }
    }

    // ── Account ────────────────────────────────────────────────────────────

    @Test fun `AccountDto toEntity maps all fields`() {
        val dto = AccountDto(id = "acc-1", personalInfoId = 10, roleId = 2, state = AccountStateEnumDto.ACTIVE)
        val entity = dto.toEntity()
        assertEquals("acc-1", entity.id)
        assertEquals(10, entity.personalInfoId)
        assertEquals(2, entity.roleId)
        assertEquals(AccountStateEnumEntity.ACTIVE, entity.state)
    }

    @Test fun `AccountDto toEntity with null state`() {
        val dto = AccountDto(id = "acc-1", personalInfoId = null, roleId = null, state = null)
        assertNull(dto.toEntity().state)
    }

    @Test fun `AccountEntity toDto maps all fields`() {
        val entity = AccountEntity(id = "acc-1", personalInfoId = 10, roleId = 2, state = AccountStateEnumEntity.INACTIVE)
        val dto = entity.toDto()
        assertEquals("acc-1", dto.id)
        assertEquals(AccountStateEnumDto.INACTIVE, dto.state)
    }

    @Test fun `roundtrip Account dto-entity-dto`() {
        val dto = AccountDto(id = "acc-1", personalInfoId = 5, roleId = 1, state = AccountStateEnumDto.ACTIVE)
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List AccountDto toEntity maps all elements`() {
        val list = listOf(
            AccountDto(id = "acc-1", state = AccountStateEnumDto.ACTIVE),
            AccountDto(id = "acc-2", state = AccountStateEnumDto.INACTIVE),
        )
        val entities = list.toEntity()
        assertEquals(2, entities.size)
        assertEquals("acc-1", entities[0].id)
        assertEquals("acc-2", entities[1].id)
    }

    // ── PersonalInformation ────────────────────────────────────────────────

    @Test fun `PersonalInformationDto toEntity maps all fields`() {
        val dto = PersonalInformationDto(
            id = 1,
            firstname = "Alice",
            lastname = "Dupont",
            email = "alice@mail.fr",
            address = "1 rue X",
            phoneNumber = "0600000000",
        )
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("Alice", entity.firstname)
        assertEquals("Dupont", entity.lastname)
        assertEquals("alice@mail.fr", entity.email)
        assertEquals("1 rue X", entity.address)
        assertEquals("0600000000", entity.phoneNumber)
    }

    @Test fun `PersonalInformationEntity toDto maps all fields`() {
        val entity = PersonalInformationEntity(
            id = 1,
            firstname = "Alice",
            lastname = "Dupont",
            email = "alice@mail.fr",
            address = "1 rue X",
            phoneNumber = "0600000000",
        )
        val dto = entity.toDto()
        assertEquals("Alice", dto.firstname)
        assertEquals("alice@mail.fr", dto.email)
    }

    @Test fun `roundtrip PersonalInformation dto-entity-dto`() {
        val dto = PersonalInformationDto(
            id = 2,
            firstname = "Bob",
            lastname = "Martin",
            email = "bob@mail.fr",
            address = "2 rue Y",
            phoneNumber = "0700000000",
        )
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List PersonalInformationDto toEntity`() {
        val list = listOf(
            PersonalInformationDto(id = 1, firstname = "A", lastname = "B", email = "a@b.fr"),
            PersonalInformationDto(id = 2, firstname = "C", lastname = "D", email = "c@d.fr"),
        )
        assertEquals(2, list.toEntity().size)
    }

    // ── PersonalInformationRegister ────────────────────────────────────────

    @Test fun `PersonalInformationRegisterEntity toDto roundtrip`() {
        val entity = PersonalInformationRegisterEntity(
            firstname = "Alice",
            lastname = "Dupont",
            email = "alice@mail.fr",
            address = "1 rue",
            phoneNumber = "06",
        )
        val dto = entity.toDto()
        assertEquals("Alice", dto.firstname)
        assertEquals(entity, dto.toEntity())
    }

    // ── Role ───────────────────────────────────────────────────────────────

    @Test fun `RoleDto toEntity maps fields`() {
        val dto = RoleDto(id = 1, name = "ADMIN")
        val entity = dto.toEntity()
        assertEquals(1, entity.id)
        assertEquals("ADMIN", entity.name)
    }

    @Test fun `RoleEntity toDto maps fields`() {
        val entity = RoleEntity(id = 2, name = "CUSTOMER")
        val dto = entity.toDto()
        assertEquals(2, dto.id)
        assertEquals("CUSTOMER", dto.name)
    }

    @Test fun `roundtrip Role dto-entity-dto`() {
        val dto = RoleDto(id = 3, name = "TEST")
        assertEquals(dto, dto.toEntity().toDto())
    }

    @Test fun `List RoleDto toEntity`() {
        val list = listOf(RoleDto(id = 1, name = "A"), RoleDto(id = 2, name = "B"))
        assertEquals(2, list.toEntity().size)
    }

    // ── SignInRequest ──────────────────────────────────────────────────────

    @Test fun `SignInRequestEntity toDto maps fields`() {
        val entity = SignInRequestEntity(id = "alice@mail.fr", password = "pass")
        val dto = entity.toDto()
        assertEquals("alice@mail.fr", dto.id)
        assertEquals("pass", dto.password)
    }

    @Test fun `roundtrip SignInRequest entity-dto-entity`() {
        val entity = SignInRequestEntity(id = "test@mail.fr", password = "secret")
        assertEquals(entity, entity.toDto().toEntity())
    }

    // ── TokenRequest ──────────────────────────────────────────────────────

    @Test fun `TokenRequestEntity toDto maps jwt`() {
        val entity = TokenRequestEntity(jwt = "my-jwt")
        assertEquals("my-jwt", entity.toDto().jwt)
    }

    @Test fun `roundtrip TokenRequest entity-dto-entity`() {
        val entity = TokenRequestEntity(jwt = "abc")
        assertEquals(entity, entity.toDto().toEntity())
    }

    // ── TokenResponse ──────────────────────────────────────────────────────

    @Test fun `TokenResponseDto toEntity maps id and role`() {
        val dto = TokenResponseDto(id = "acc-1", role = "ADMIN")
        val entity = dto.toEntity()
        assertEquals("acc-1", entity.id)
        assertEquals("ADMIN", entity.role)
    }

    @Test fun `roundtrip TokenResponse dto-entity-dto`() {
        val dto = TokenResponseDto(id = "acc-1", role = "CUSTOMER")
        assertEquals(dto, dto.toEntity().toDto())
    }

    // ── AccountRegister ────────────────────────────────────────────────────
    // AccountRegister.role est de type RoleEnum (pas Role)

    @Test fun `AccountRegisterEntity toDto maps all fields`() {
        val entity = AccountRegisterEntity(
            personalInfo = PersonalInformationRegisterEntity(
                firstname = "Alice",
                lastname = "Dupont",
                email = "alice@mail.fr",
                address = "1 rue",
                phoneNumber = "06",
            ),
            role = RoleEnumEntity.CUSTOMER,
            password = "Password1!",
        )
        val dto = entity.toDto()
        assertEquals("Alice", dto.personalInfo.firstname)
        assertEquals("Password1!", dto.password)
    }

    @Test fun `roundtrip AccountRegister entity-dto-entity`() {
        val entity = AccountRegisterEntity(
            personalInfo = PersonalInformationRegisterEntity(
                firstname = "Bob",
                lastname = "Martin",
                email = "bob@mail.fr",
                address = "2 rue",
                phoneNumber = "07",
            ),
            role = RoleEnumEntity.ADMIN,
            password = "Admin1!",
        )
        assertEquals(entity, entity.toDto().toEntity())
    }
}
