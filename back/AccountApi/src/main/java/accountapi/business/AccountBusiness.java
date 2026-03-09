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
        List<AccountEntity> accounts;
        try {
            accounts = accountRepository.findAll();
        } catch (Exception e) {
            throw new NotFoundException("404", "Impossible de récupérer les comptes");
        }

        List<Account> dtos = new ArrayList<>();
        for (AccountEntity account : accounts) {
            dtos.add(AccountMapper.toDto(account));
        }
        return dtos;
    }

    public Account getAccountById(String id) {
        AccountEntity accountEntity;
        try {
            accountEntity = accountRepository.findById(id);
        } catch (Exception e) {
            throw new NotFoundException("404", "Compte non trouvé avec l'ID : " + id);
        }
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
        Role roleToRegister = roleBusiness.getRoleByName(account.getRole().name());

        if (roleToRegister == null) {
            throw new FunctionalException("400", "Le Role n'existe pas : " + account.getRole().name());
        }

        PersonalInformation pInfo = personalInformationBusiness.createPersonalInformation(account.getPersonalInfo());

        if (pInfo == null) {
            throw new FunctionalException("400", "Impossible de créer les informations personnelles du compte");
        }

        Integer pif = pInfo.getId();

        AccountEntity accountToRegister = new AccountEntity();
        accountToRegister.setId(idGenerated);
        accountToRegister.setRoleId(roleToRegister.getId());
        accountToRegister.setState(AccountStateEnum.INACTIVE);
        accountToRegister.setPersonalInfoId(pif);
        accountToRegister.setPassword(account.getPassword());

        AccountEntity registered = accountRepository.register(accountToRegister);
        if (registered == null) {
            // Delete personal information lorsque la création du compte a échoué (évite d'avoir des personal info sauvage)
            personalInformationBusiness.deletePersonalInformation(pif);
            throw new FunctionalException("400", "Impossible de créer le compte");
        }
        return AccountMapper.toDto(registered);
    }

    public boolean deleteAccount(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountEntity.setState(AccountStateEnum.ENCLOSE);
        try {
            accountRepository.updateState(accountEntity);
        } catch (Exception e) {
            throw new NotFoundException("404", "Impossible de supprimer le compte avec l'ID : " + id);
        }
        return true;
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
        AccountEntity accountEntity;
        try {
            accountEntity = accountRepository.getAccountByIdAndPassword(signInRequest.getId(), signInRequest.getPassword());
        } catch (Exception e) {
            throw new NotFoundException("404", "Compte non trouvé");
        }

        if (accountEntity == null) {
            throw new NotFoundException("404", "Compte non trouvé");
        }

        if (accountEntity.getId().equals(signInRequest.getId()) &&
                accountEntity.getPassword().equals(signInRequest.getPassword()) &&
                    accountEntity.getState().equals(AccountStateEnum.ACTIVE)) {
            RoleEntity roleEntity = RoleMapper.toEntity(this.getRoleByAccountId(accountEntity.getId()));
            String key = keyJWT.generateKey(accountEntity.getId(), roleEntity.getName());

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setJwt(key);
            return tokenRequest;
        }
        throw new UnauthorizedException("401", "Mot de passe incorrect ou compte inactif");
    }

    public TokenResponse validateToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getJwt();
        TokenResponse tokenResponse;
        try {
            tokenResponse = keyJWT.validateToken(jwt);
        } catch (Exception e) {
            throw new UnauthorizedException("401", "Token invalide ou expiré");
        }

        if (tokenResponse != null && tokenResponse.getId() != null && !tokenResponse.getId().isEmpty()) {
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
        if (accountEntity.getState().equals(AccountStateEnum.ENCLOSE)) {
            throw new FunctionalException("400", "Impossible de désactiver un compte clôturé");
        }
        if (accountEntity.getState().equals(AccountStateEnum.INACTIVE)) {
            throw new FunctionalException("400", "Le compte est déjà inactif");
        }
        accountEntity.setState(AccountStateEnum.INACTIVE);
        try {
            accountRepository.updateState(accountEntity);
        } catch (Exception e) {
            throw new FunctionalException("400", "Impossible de désactiver le compte : " + e.getMessage());
        }
        return true;
    }

    public boolean activateAccount(String id) {
        Account account = this.getAccountById(id);
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        if (accountEntity.getState().equals(AccountStateEnum.ENCLOSE)) {
            throw new FunctionalException("400", "Impossible d'activer un compte clôturé");
        }
        if (accountEntity.getState().equals(AccountStateEnum.ACTIVE)) {
            throw new FunctionalException("400", "Le compte est déjà actif");
        }
        accountEntity.setState(AccountStateEnum.ACTIVE);
        try {
            accountRepository.updateState(accountEntity);
        } catch (Exception e) {
            throw new FunctionalException("400", "Impossible d'activer le compte : " + e.getMessage());
        }
        return true;
    }
}