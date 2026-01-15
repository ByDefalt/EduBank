package accountapi.business;

import accountapi.entity.RoleEntity;
import accountapi.repository.RoleRepository;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleBusiness {

    @Inject
    private RoleRepository roleRepository;

    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    public RoleEntity getRoleById(Integer id) {
        return roleRepository.findById(id);
    }
}
