package gatewayapi.business.account;

import dto.accountapi.*;
import gatewayapi.repository.account.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountBusiness {

    private final AccountRepository accountRepository;

    public AccountBusiness(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(AccountRegister accountRegister) {
        return accountRepository.register(accountRegister);
    }

    public boolean deleteAccount(String id) {
        return accountRepository.delete(id);
    }

    public boolean activateAccount(String id) {
        return accountRepository.activateAccount(id);
    }

    public boolean deactivateAccount(String id) {
        return accountRepository.deactivateAccount(id);
    }

    public Role getRoleByAccountId(String id) {
        return accountRepository.getRoleByAccountId(id);
    }

    public PersonalInformation getPersonalInformationByAccountId(String id) {
        return accountRepository.getPersonalInformationByAccountId(id);
    }

    public TokenRequest signIn(SignInRequest signInRequest) {
        return accountRepository.signIn(signInRequest);
    }

    public TokenResponse validateToken(TokenRequest tokenRequest) {
        return accountRepository.validateToken(tokenRequest);
    }
}
