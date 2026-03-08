package gatewayapi.client;

import dto.accountapi.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface AccountClient {

    // --- AccountController ---
    @RequestLine("GET /accounts")
    @Headers("Content-Type: application/json")
    List<Account> getAllAccounts();

    @RequestLine("GET /accounts/{id}")
    @Headers("Content-Type: application/json")
    Account getAccountById(@Param("id") String id);

    @RequestLine("POST /accounts")
    @Headers("Content-Type: application/json")
    Account createAccount(AccountRegister accountDto);

    @RequestLine("DELETE /accounts/{id}")
    @Headers("Content-Type: application/json")
    boolean deleteAccount(@Param("id") String id);

    @RequestLine("PUT /accounts/activate/{id}")
    @Headers("Content-Type: application/json")
    boolean activateAccount(@Param("id") String id);

    @RequestLine("PUT /accounts/deactivate/{id}")
    @Headers("Content-Type: application/json")
    boolean deactivateAccount(@Param("id") String id);

    @RequestLine("GET /accounts/role/{id}")
    @Headers("Content-Type: application/json")
    Role getRoleByAccountId(@Param("id") String id);

    @RequestLine("GET /accounts/personalInformation/{id}")
    @Headers("Content-Type: application/json")
    PersonalInformation getPersonalInformationByAccountId(@Param("id") String id);

    @RequestLine("POST /accounts/signin")
    @Headers("Content-Type: application/json")
    TokenRequest signIn(SignInRequest signInRequest);

    @RequestLine("POST /accounts/validate")
    @Headers("Content-Type: application/json")
    TokenResponse validateToken(TokenRequest tokenRequest);

    // --- PersonalInformationController ---
    @RequestLine("GET /personalInformation")
    @Headers("Content-Type: application/json")
    List<PersonalInformation> getAllPersonalInformation();

    @RequestLine("GET /personalInformation/{id}")
    @Headers("Content-Type: application/json")
    PersonalInformation getPersonalInformationById(@Param("id") Integer id);

    @RequestLine("POST /personalInformation")
    @Headers("Content-Type: application/json")
    PersonalInformation createPersonalInformation(PersonalInformationRegister personalInformationRegister);

    @RequestLine("PUT /personalInformation/{id}")
    @Headers("Content-Type: application/json")
    PersonalInformation updatePersonalInformation(@Param("id") Integer id, PersonalInformation personalInformation);

    // --- RoleController ---
    @RequestLine("GET /roles")
    @Headers("Content-Type: application/json")
    List<Role> getAllRoles();

    @RequestLine("GET /roles/{id}")
    @Headers("Content-Type: application/json")
    Role getRoleById(@Param("id") Integer id);

    @RequestLine("GET /roles/name/{name}")
    @Headers("Content-Type: application/json")
    Role getRoleByName(@Param("name") String name);
}