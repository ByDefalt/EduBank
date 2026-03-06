package gatewayapi.repository.account;

import dto.accountapi.Role;
import gatewayapi.client.AccountClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleRepository {

    private final AccountClient accountClient;

    public RoleRepository(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public List<Role> findAll() {
        return accountClient.getAllRoles();
    }

    public Role findById(Integer id) {
        return accountClient.getRoleById(id);
    }

    public Role findByName(String name) {
        return accountClient.getRoleByName(name);
    }
}
