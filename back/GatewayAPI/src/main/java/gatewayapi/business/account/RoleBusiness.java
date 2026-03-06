package gatewayapi.business.account;

import dto.accountapi.Role;
import gatewayapi.repository.account.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleBusiness {

    private final RoleRepository roleRepository;

    public RoleBusiness(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id);
    }
}

