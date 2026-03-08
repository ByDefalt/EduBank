package accountapi.mapper;

import accountapi.entity.AccountEntity;
import accountapi.entity.PersonalInformationEntity;
import accountapi.entity.RoleEntity;
import dto.accountapi.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    @Test
    void testAccountMapperToDto() {
        AccountEntity entity = new AccountEntity();
        entity.setId("ACC123456789");
        entity.setPersonalInfoId(100);
        entity.setRoleId(2);
        entity.setState(AccountStateEnum.ACTIVE);

        Account dto = AccountMapper.toDto(entity);

        assertEquals("ACC123456789", dto.getId());
        assertEquals(100, dto.getPersonalInfoId());
        assertEquals(2, dto.getRoleId());
        assertEquals("ACTIVE", dto.getState().name());
    }

    @Test
    void testAccountMapperToDtoNull() {
        assertNull(AccountMapper.toDto(null));
    }

    @Test
    void testAccountMapperToEntity() {
        Account dto = new Account();
        dto.setId("ACC123456789");
        dto.setPersonalInfoId(100);
        dto.setRoleId(2);
        dto.setState(AccountStateEnum.INACTIVE);

        AccountEntity entity = AccountMapper.toEntity(dto);

        assertEquals("ACC123456789", entity.getId());
        assertEquals(100, entity.getPersonalInfoId());
        assertEquals(2, entity.getRoleId());
        assertEquals("INACTIVE", entity.getState().name());
    }

    @Test
    void testAccountMapperToEntityNull() {
        assertNull(AccountMapper.toEntity(null));
    }

    @Test
    void testPersonalInformationMapperToDto() {
        PersonalInformationEntity entity = new PersonalInformationEntity();
        entity.setId(1);
        entity.setFirstname("Jean");
        entity.setLastname("Martin");
        entity.setEmail("jean.martin@example.com");
        entity.setAddress("123 Rue de la Paix");
        entity.setPhoneNumber("0123456789");

        PersonalInformation dto = PersonalInformationMapper.toDto(entity);

        assertEquals(1, dto.getId());
        assertEquals("Jean", dto.getFirstname());
        assertEquals("Martin", dto.getLastname());
        assertEquals("jean.martin@example.com", dto.getEmail());
        assertEquals("123 Rue de la Paix", dto.getAddress());
        assertEquals("0123456789", dto.getPhoneNumber());
    }

    @Test
    void testPersonalInformationMapperToDtoNull() {
        assertNull(PersonalInformationMapper.toDto(null));
    }

    @Test
    void testPersonalInformationMapperToEntity() {
        PersonalInformationRegister register = new PersonalInformationRegister();
        register.setFirstname("Jean");
        register.setLastname("Martin");
        register.setEmail("jean.martin@example.com");
        register.setAddress("123 Rue de la Paix");
        register.setPhoneNumber("0123456789");

        PersonalInformationEntity entity = PersonalInformationMapper.toEntity(register);

        assertNull(entity.getId());
        assertEquals("Jean", entity.getFirstname());
        assertEquals("Martin", entity.getLastname());
        assertEquals("jean.martin@example.com", entity.getEmail());
        assertEquals("123 Rue de la Paix", entity.getAddress());
        assertEquals("0123456789", entity.getPhoneNumber());
    }

    @Test
    void testPersonalInformationMapperToEntityFromDto() {
        PersonalInformation dto = new PersonalInformation();
        dto.setId(100);
        dto.setFirstname("Jean");
        dto.setLastname("Dupont");
        dto.setEmail("jean.dupont@example.com");
        dto.setAddress("456 Avenue des Fleurs");
        dto.setPhoneNumber("+33612345678");

        PersonalInformationEntity entity = PersonalInformationMapper.toEntity(dto);

        assertEquals("Jean", entity.getFirstname());
        assertEquals("Dupont", entity.getLastname());
        assertEquals("jean.dupont@example.com", entity.getEmail());
        assertEquals("456 Avenue des Fleurs", entity.getAddress());
        assertEquals("+33612345678", entity.getPhoneNumber());
    }

    @Test
    void testPersonalInformationMapperToEntityFromDtoNull() {
        PersonalInformation dto = null;
        assertNull(PersonalInformationMapper.toEntity(dto));
    }

    @Test
    void testPersonalInformationMapperToEntityNull() {
        assertNull(PersonalInformationMapper.toEntity((PersonalInformationRegister) null));
    }

    @Test
    void testRoleMapperToDto() {
        RoleEntity entity = new RoleEntity();
        entity.setId(1);
        entity.setName("ADMIN");

        Role dto = RoleMapper.toDto(entity);

        assertEquals("ADMIN", dto.getName());
    }

    @Test
    void testRoleMapperToDtoNull() {
        assertNull(RoleMapper.toDto(null));
    }

    @Test
    void testRoleMapperToEntity() {
        Role dto = new Role();
        dto.setId(2);
        dto.setName("CUSTOMER");

        RoleEntity entity = RoleMapper.toEntity(dto);

        assertEquals(2, entity.getId());
        assertEquals("CUSTOMER", entity.getName());
    }

    @Test
    void testRoleMapperToEntityNull() {
        assertNull(RoleMapper.toEntity(null));
    }
}

