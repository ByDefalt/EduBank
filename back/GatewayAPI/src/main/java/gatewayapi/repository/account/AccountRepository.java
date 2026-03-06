package gatewayapi.repository.account;

import dto.accountapi.*;
import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountRepository {

    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    public AccountRepository(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    public List<Account> findAll() {
        return accountClient.getAllAccounts();
    }

    public Account findById(String id) {
        return accountClient.getAccountById(id);
    }

    public Account register(AccountRegister accountRegister) {
        return accountClient.createAccount(accountRegister);
    }

    public TokenRequest getAccountByIdAndPassword(SignInRequest signInRequest) {
        return accountClient.signIn(signInRequest);
    }

    public TokenResponse validateToken(TokenRequest tokenRequest) {
        return accountClient.validateToken(tokenRequest);
    }

    public boolean delete(String id) {
        return accountClient.deleteAccount(id);
    }

    public Role getRoleByAccountId(String id) {
        return accountClient.getRoleByAccountId(id);
    }

    public PersonalInformation getPersonalInformationByAccountId(String id) {
        return accountClient.getPersonalInformationByAccountId(id);
    }
    
    public boolean deactivateAccount(String id) {
        return accountClient.deactivateAccount(id);
    }
    
    public boolean activateAccount(String id) {
        return accountClient.activateAccount(id);
    }
}
