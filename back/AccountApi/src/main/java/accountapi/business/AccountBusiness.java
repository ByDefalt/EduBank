package accountapi.business;

import accountapi.entity.AccountEntity;
import accountapi.entity.PersonalInformationEntity;
import accountapi.entity.RoleEntity;
import accountapi.repository.AccountRepository;
import accountapi.utils.GenerateID;
import accountapi.utils.JwtUtils;
import dto.accountapi.AccountRegister;
import dto.accountapi.SignInRequest;
import dto.accountapi.TokenRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;
import java.util.List;

import static accountapi.mapper.PersonalInformationMapper.toEntity;

@Service
public class AccountBusiness {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private PersonalInformationBusiness personalInformationBusiness;

    @Inject
    private RoleBusiness roleBusiness;

    private final JwtUtils keyJWT = new JwtUtils();

    public List<AccountEntity> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountEntity getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public AccountEntity createAccount(AccountRegister account) {
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

        PersonalInformationEntity pif =  personalInformationBusiness.createPersonalInformation(toEntity(account.getPersonalInfo()));

        AccountEntity accountToRegister = new AccountEntity();
        accountToRegister.setId(idGenerated);
        accountToRegister.setRoleId(account.getRoleId());
        accountToRegister.setState(account.getState());
        accountToRegister.setPersonalInfoId(pif.getId());
        accountToRegister.setPassword(account.getPassword());

        return accountRepository.register(accountToRegister);
    }

    public boolean deleteAccount(String id) {
        boolean deleted = false;
        AccountEntity accountEntity = this.getAccountById(id);
        if (accountEntity == null) {
            return false;
        }

        deleted = accountRepository.delete(id);
        if (deleted){
            personalInformationBusiness.deletePersonalInformation(accountEntity.getPersonalInfoId());
        }
        return deleted;
    }

    public RoleEntity getRoleByAccountId(String accountId) {
        AccountEntity account = this.getAccountById(accountId);
        if (account == null) {
            return null;
        }
        return roleBusiness.getRoleById(account.getRoleId());
    }

    public PersonalInformationEntity getPersonalInformationByAccountId(String id) {
        AccountEntity account = this.getAccountById(id);
        if (account == null) {
            return null;
        }
        return personalInformationBusiness.getPersonalInformationById(account.getPersonalInfoId());
    }

    public Response signIn(SignInRequest signInRequest) {
        String key = "";

        AccountEntity accountEntity = accountRepository.getAccountByIdAndPassword(signInRequest.getId(), signInRequest.getPassword());

        if (accountEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (accountEntity.getId().equals(signInRequest.getId()) &&
                accountEntity.getPassword().equals(signInRequest.getPassword()) &&
                    accountEntity.getState().equals("ACTIVE")) {
            // get le role
            RoleEntity roleEntity = this.getRoleByAccountId(accountEntity.getId());
            // genere le token avec l'id et le role
            key = keyJWT.generateKey(accountEntity.getId(), roleEntity.getName());

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setJwt(key);
            return Response.status(Response.Status.OK).entity(tokenRequest).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public Response validateToken(TokenRequest tokenRequest) {
        String jwt = tokenRequest.getJwt();
        String id = keyJWT.validateToken(jwt);

        if (id != null && !id.isEmpty()) {
            AccountEntity acc = accountRepository.findById(id);
            if (acc.getId().equals(id)) {
                TokenRequest tokenRequest2 = new TokenRequest();
                tokenRequest2.setJwt(jwt);
                return Response.status(Response.Status.OK).entity(tokenRequest2).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    public boolean deactivateAccount(String id) {
        AccountEntity accountEntity = this.getAccountById(id);
        if (accountEntity == null) {
            return false;
        }
        accountEntity.setState("INACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }

    public boolean activateAccount(String id) {
        AccountEntity accountEntity = this.getAccountById(id);
        if (accountEntity == null) {
            return false;
        }
        accountEntity.setState("ACTIVE");
        accountRepository.updateState(accountEntity);
        return true;
    }
}