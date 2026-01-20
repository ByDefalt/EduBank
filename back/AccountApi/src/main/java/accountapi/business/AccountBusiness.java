package accountapi.business;

import accountapi.entity.AccountEntity;
import accountapi.entity.RoleEntity;
import accountapi.mapper.AccountMapper;
import accountapi.mapper.RoleMapper;
import accountapi.repository.AccountRepository;
import accountapi.utils.GenerateID;
import accountapi.utils.JwtUtils;
import dto.accountapi.*;
import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountBusiness {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private PersonalInformationBusiness personalInformationBusiness;

    @Inject
    private RoleBusiness roleBusiness;

    private final JwtUtils keyJWT = new JwtUtils();

    public List<Account> getAllAccounts() {
        List<AccountEntity> accounts = accountRepository.findAll();

        List<Account> dtos = new ArrayList<>();
        for (AccountEntity account : accounts) {
            dtos.add(AccountMapper.toDto(account));
        }
        return dtos;
    }

    public Account getAccountById(String id) {
        return AccountMapper.toDto(accountRepository.findById(id));
    }

    public Account createAccount(AccountRegister account) {
        String idGenerated = (String.valueOf(GenerateID.generateId()));

        AccountEntity accountEntity = accountRepository.findById(idGenerated);
        if (accountEntity != null) {
            while (accountEntity != null){
                accountEntity = accountRepository.findById(idGenerated);
                if (accountEntity == null)
                    break;
                idGenerated = (String.valueOf(GenerateID.generateId()));
            }
        }

        Integer pif = personalInformationBusiness.createPersonalInformation(account.getPersonalInfo()).getId();

        if (pif == null) {
            return null;
        }

        AccountEntity accountToRegister = new AccountEntity();
        accountToRegister.setId(idGenerated);
        accountToRegister.setRoleId(account.getRoleId());
        accountToRegister.setState(account.getState());
        accountToRegister.setPersonalInfoId(pif);
        accountToRegister.setPassword(account.getPassword());

        return AccountMapper.toDto(accountRepository.register(accountToRegister));
    }

    public boolean deleteAccount(String id) {
        boolean deleted = false;
        AccountEntity accountEntity = AccountMapper.toEntity(this.getAccountById(id));
        if (accountEntity == null) {
            return false;
        }

        deleted = accountRepository.delete(id);
        if (deleted){
            personalInformationBusiness.deletePersonalInformation(accountEntity.getPersonalInfoId());
        }
        return deleted;
    }

    public Role getRoleByAccountId(String id) {
        AccountEntity accountEntity = AccountMapper.toEntity(this.getAccountById(id));
        if (accountEntity == null) {
            return null;
        }
        return roleBusiness.getRoleById(accountEntity.getRoleId());
    }

    public PersonalInformation getPersonalInformationByAccountId(String id) {
        AccountEntity accountEntity = AccountMapper.toEntity(this.getAccountById(id));
        if (accountEntity == null) {
            return null;
        }
        return personalInformationBusiness.getPersonalInformationById(accountEntity.getPersonalInfoId());
    }

    public TokenRequest signIn(SignInRequest signInRequest) {
        String key = "";

        AccountEntity accountEntity = accountRepository.getAccountByIdAndPassword(signInRequest.getId(), signInRequest.getPassword());

        if (accountEntity == null) {
            return null;
        }
        if (accountEntity.getId().equals(signInRequest.getId()) &&
                accountEntity.getPassword().equals(signInRequest.getPassword()) &&
                    accountEntity.getState().equals("ACTIVE")) {
            // get le role
            RoleEntity roleEntity = RoleMapper.toEntity(this.getRoleByAccountId(accountEntity.getId()));
            // genere le token avec l'id et le role
            key = keyJWT.generateKey(accountEntity.getId(), roleEntity.getName());

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setJwt(key);
            return tokenRequest;
        }
        return null;
    }

    public TokenResponse validateToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getJwt();
        TokenResponse tokenResponse = keyJWT.validateToken(jwt);

        if (tokenResponse.getId() != null && !tokenResponse.getId().isEmpty()) {
            AccountEntity acc = accountRepository.findById(tokenResponse.getId());
            if (acc.getId().equals(tokenResponse.getId())) {
                return tokenResponse;
            }
        }
        return null;
    }

    public boolean deactivateAccount(String id) {
        AccountEntity accountEntity = AccountMapper.toEntity(this.getAccountById(id));
        if (accountEntity == null) {
            return false;
        }
        accountEntity.setState("INACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }

    public boolean activateAccount(String id) {
        AccountEntity accountEntity = AccountMapper.toEntity(this.getAccountById(id));
        if (accountEntity == null) {
            return false;
        }
        accountEntity.setState("ACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }
}