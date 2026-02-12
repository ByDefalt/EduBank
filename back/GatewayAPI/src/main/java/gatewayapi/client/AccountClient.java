package gatewayapi.client;

import dto.accountapi.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.util.List;

public interface AccountClient {

    // AccountController
    @RequestLine("GET /accounts")
    @Headers("Content-Type: application/json")
    List<Account> getAllAccounts();

    @RequestLine("GET /accounts/{idAccount}")
    @Headers("Content-Type: application/json")
    Account getAccountById(@Param("idAccount") String idAccount);

    @RequestLine("POST /accounts")
    @Headers("Content-Type: application/json")
    Account createAccount(AccountRegister accountDto);

    @RequestLine("POST /accounts/signin")
    @Headers("Content-Type: application/json")
    TokenRequest signIn(SignInRequest signInRequest);

    @RequestLine("POST /accounts/validate")
    @Headers("Content-Type: application/json")
    TokenResponse validateToken(TokenRequest tokenRequest);

    @RequestLine("DELETE /accounts/{idAccount}")
    @Headers("Content-Type: application/json")
    boolean deleteAccount(@Param("idAccount") String idAccount);

    @RequestLine("GET /accounts/role/{idAccount}")
    @Headers("Content-Type: application/json")
    Role getRoleByAccountId(@Param("idAccount") String idAccount);

    @RequestLine("GET /accounts/personalInformation/{idAccount}")
    @Headers("Content-Type: application/json")
    PersonalInformation getPersonalInformationByAccountId(@Param("idAccount") String idAccount);

    @RequestLine("PUT /accounts/deactivate/{idAccount}")
    @Headers("Content-Type: application/json")
    boolean deactivateAccount(@Param("idAccount") String idAccount);

    @RequestLine("PUT /accounts/activate/{idAccount}")
    @Headers("Content-Type: application/json")
    boolean activateAccount(@Param("idAccount") String idAccount);

    // PersonalInformationController
    @RequestLine("GET /personalInformation")
    @Headers("Content-Type: application/json")
    List<PersonalInformation> getAllPersonalInformation();

    @RequestLine("GET /personalInformation/{id}")
    @Headers("Content-Type: application/json")
    PersonalInformation getPersonalInformationById(@Param("id") Integer id);

    // --- RoleController ---
    @RequestLine("GET /roles")
    @Headers("Content-Type: application/json")
    List<Role> getAllRoles();

    @RequestLine("GET /roles/{id}")
    @Headers("Content-Type: application/json")
    Role getRoleById(@Param("id") Integer id);
}