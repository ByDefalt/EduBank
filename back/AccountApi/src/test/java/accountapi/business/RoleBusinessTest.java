package accountapi.business;

import accountapi.entity.RoleEntity;
import accountapi.repository.RoleRepository;
import dto.accountapi.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static accountapi.mapper.RoleMapper.toDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleBusinessTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleBusiness roleBusiness;

    @Test
    void testGetAllRoles() {
        RoleEntity adminEntity = new RoleEntity();
        adminEntity.setId(1);
        adminEntity.setName("ADMIN");

        RoleEntity customerEntity = new RoleEntity();
        customerEntity.setId(2);
        customerEntity.setName("CUSTOMER");

        Role adminRole = toDto(adminEntity);
        Role customerRole = toDto(customerEntity);

        when(roleRepository.findAll()).thenReturn(java.util.Arrays.asList(adminEntity, customerEntity));

        List<Role> roles = roleBusiness.getAllRoles();

        assertEquals(2, roles.size());
        assertEquals(adminRole.getName(), roles.get(0).getName());
        assertEquals(customerRole.getName(), roles.get(1).getName());
    }

    @Test
    void testGetRoleById() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2);
        roleEntity.setName("CUSTOMER");

        Role role = toDto(roleEntity);

        when(roleRepository.findById(2)).thenReturn(roleEntity);

        Role roleResponse = roleBusiness.getRoleById(2);

        assertEquals(role.getName(), roleResponse.getName());
    }
}