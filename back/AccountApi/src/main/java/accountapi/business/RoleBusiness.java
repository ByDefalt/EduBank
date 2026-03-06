package accountapi.business;

import accountapi.entity.RoleEntity;
import accountapi.exception.NotFoundException;
import accountapi.mapper.RoleMapper;
import accountapi.repository.RoleRepository;
import dto.accountapi.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleBusiness {

    private RoleRepository roleRepository;

    public RoleBusiness(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        List<RoleEntity> roles = roleRepository.findAll();

        List<Role> dtos = new ArrayList<>();
        for (RoleEntity role : roles) {
            dtos.add(RoleMapper.toDto(role));
        }
        return dtos;
    }

    public Role getRoleById(Integer id) {
        RoleEntity roleEntity = roleRepository.findById(id);
        if (roleEntity == null) {
            throw new NotFoundException("404", "Rôle non trouvé avec l'ID : " + id);
        }
        return RoleMapper.toDto(roleEntity);
    }

    public Role getRoleByName(String name) {
        RoleEntity roleEntity = roleRepository.findByName(name);
        if (roleEntity == null) {
            throw new NotFoundException("404", "Rôle non trouvé avec l'ID : " + name);
        }
        return RoleMapper.toDto(roleEntity);
    }
}
