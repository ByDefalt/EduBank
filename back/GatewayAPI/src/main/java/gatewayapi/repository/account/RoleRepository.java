package gatewayapi.repository.account;

import dto.accountapi.Role;
import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleRepository {

    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    public RoleRepository(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    public List<Role> findAll() {
        return feignExecutor.execute(() -> accountClient.getAllRoles());
    }

    public Role findById(Integer id) {
        return feignExecutor.execute(() -> accountClient.getRoleById(id));
    }
}

