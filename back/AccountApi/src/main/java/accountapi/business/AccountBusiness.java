package accountapi.business;

import accountapi.entity.AccountEntity;
import accountapi.entity.RoleEntity;
import accountapi.exception.FunctionalException;
import accountapi.exception.NotFoundException;
import accountapi.exception.UnauthorizedException;
import accountapi.mapper.AccountMapper;
import accountapi.mapper.RoleMapper;
import accountapi.repository.AccountRepository;
import accountapi.utils.GenerateID;
import accountapi.utils.JwtUtils;
import dto.accountapi.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountBusiness {

    private AccountRepository accountRepository;
    private PersonalInformationBusiness personalInformationBusiness;
    private RoleBusiness roleBusiness;

    public AccountBusiness(AccountRepository accountRepository,
                           PersonalInformationBusiness personalInformationBusiness,
                           RoleBusiness roleBusiness) {
        this.accountRepository = accountRepository;
        this.personalInformationBusiness = personalInformationBusiness;
        this.roleBusiness = roleBusiness;
    }

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
        AccountEntity accountEntity = accountRepository.findById(id);
        if (accountEntity == null) {
            throw new NotFoundException("404", "Compte non trouvé avec l'ID : " + id);
        }
        return AccountMapper.toDto(accountEntity);
    }

    public Account createAccount(AccountRegister account) {
        String idGenerated = GenerateID.generateId();

        AccountEntity accountEntity = accountRepository.findById(idGenerated);
        if (accountEntity != null) {
            while (accountEntity != null){
                accountEntity = accountRepository.findById(idGenerated);
                if (accountEntity == null)
                    break;
                idGenerated = GenerateID.generateId();
            }
        }

        PersonalInformation pInfo = personalInformationBusiness.createPersonalInformation(account.getPersonalInfo());
        Integer pif = pInfo.getId();

        if (pif == null) {
            throw new FunctionalException("400", "Impossible de créer les informations personnelles du compte");
        }

        AccountEntity accountToRegister = new AccountEntity();
        accountToRegister.setId(idGenerated);
        accountToRegister.setRoleId(account.getRoleId());
        accountToRegister.setState("INACTIVE");
        accountToRegister.setPersonalInfoId(pif);
        accountToRegister.setPassword(account.getPassword());

        AccountEntity registered = accountRepository.register(accountToRegister);
        if (registered == null) {
            throw new FunctionalException("400", "Impossible de créer le compte");
        }
        return AccountMapper.toDto(registered);
    }

    public boolean deleteAccount(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);

        boolean deleted = accountRepository.delete(id);
        if (deleted){
            personalInformationBusiness.deletePersonalInformation(accountEntity.getPersonalInfoId());
        }
        return deleted;
    }

    public Role getRoleByAccountId(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        return roleBusiness.getRoleById(accountEntity.getRoleId());
    }

    public PersonalInformation getPersonalInformationByAccountId(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        return personalInformationBusiness.getPersonalInformationById(accountEntity.getPersonalInfoId());
    }

    public TokenRequest signIn(SignInRequest signInRequest) {
        String key = "";

        AccountEntity accountEntity = accountRepository.getAccountByIdAndPassword(signInRequest.getId(), signInRequest.getPassword());

        if (accountEntity == null) {
            throw new NotFoundException("404", "Compte non trouvé avec l'ID : " + signInRequest.getId());
        }
        if (accountEntity.getId().equals(signInRequest.getId()) &&
                accountEntity.getPassword().equals(signInRequest.getPassword()) &&
                    accountEntity.getState().equals("ACTIVE")) {
            RoleEntity roleEntity = RoleMapper.toEntity(this.getRoleByAccountId(accountEntity.getId()));
            key = keyJWT.generateKey(accountEntity.getId(), roleEntity.getName());

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setJwt(key);
            return tokenRequest;
        }
        throw new UnauthorizedException("401", "Mot de passe incorrect ou compte inactif");
    }

    public TokenResponse validateToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getJwt();
        TokenResponse tokenResponse = keyJWT.validateToken(jwt);

        if (tokenResponse.getId() != null && !tokenResponse.getId().isEmpty()) {
            AccountEntity acc = accountRepository.findById(tokenResponse.getId());
            if (acc != null && acc.getId().equals(tokenResponse.getId())) {
                return tokenResponse;
            }
        }
        throw new UnauthorizedException("401", "Token invalide ou expiré");
    }

    public boolean deactivateAccount(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountEntity.setState("INACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }

    public boolean activateAccount(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountEntity.setState("ACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }
}