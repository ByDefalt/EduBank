package accountapi.business;

import accountapi.entity.RoleEntity;
import accountapi.mapper.RoleMapper;
import accountapi.repository.RoleRepository;
import dto.accountapi.Role;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleBusiness {

    @Inject
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();

        List<Role> dtos = new ArrayList<>();
        for (RoleEntity role : roles) {
            dtos.add(RoleMapper.toDto(role));
        }
        return dtos;
    }

    public Role getRoleById(Integer id) {
        return RoleMapper.toDto(roleRepository.findById(id));
    }
}
