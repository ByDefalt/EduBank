package gatewayapi.client;

import dto.bankapiswagger.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface BankAccountClient {

    // ==================== ADMIN ====================

    @RequestLine("GET /bank/admin/bank-accounts")
    @Headers("Content-Type: application/json")
    List<BankAccount> getAllBankAccounts();

    @RequestLine("GET /bank/admin/bank-accounts/{id}")
    @Headers("Content-Type: application/json")
    BankAccountDetail getBankAccountById(@Param("id") String id);

    @RequestLine("GET /bank/admin/bank-accounts/iban/{iban}")
    @Headers("Content-Type: application/json")
    BankAccount getBankAccountByIban(@Param("iban") String iban);

    @RequestLine("PUT /bank/admin/bank-accounts/{id}")
    @Headers("Content-Type: application/json")
    BankAccount updateBankAccount(@Param("id") String id, BankAccount bankAccount);

    @RequestLine("DELETE /bank/admin/bank-accounts/{id}")
    @Headers("Content-Type: application/json")
    void deleteBankAccount(@Param("id") String id);

    @RequestLine("GET /bank/admin/accounts/{accountId}/bank-accounts")
    @Headers("Content-Type: application/json")
    List<BankAccount> getBankAccountsByAccountId(@Param("accountId") String accountId);

    @RequestLine("POST /bank/admin/accounts/{accountId}/bank-accounts")
    @Headers("Content-Type: application/json")
    BankAccountDetail createBankAccount(@Param("accountId") String accountId, BankAccountCreateRequest request);

    @RequestLine("PUT /bank/admin/bank-accounts/{bankAccountId}/parameters")
    @Headers("Content-Type: application/json")
    void updateParameters(@Param("bankAccountId") String bankAccountId, BankAccountParameter parameters);

    // ==================== TYPES ====================

    @RequestLine("GET /bank/admin/types")
    @Headers("Content-Type: application/json")
    List<Type> getAllTypes();

    @RequestLine("GET /bank/admin/types/{id}")
    @Headers("Content-Type: application/json")
    Type getTypeById(@Param("id") Integer id);

    @RequestLine("POST /bank/admin/types")
    @Headers("Content-Type: application/json")
    Type createType(Type type);

    // ==================== PIVOT ====================

    @RequestLine("POST /bank/bank-accounts-pivot")
    @Headers("Content-Type: application/json")
    void createPivot(BankAccountPivot pivot);

    @RequestLine("DELETE /bank/bank-accounts-pivot")
    @Headers("Content-Type: application/json")
    void deletePivot(BankAccountPivot pivot);

    @RequestLine("GET /bank/bank-accounts-pivot/bank-account/{bankAccountId}")
    @Headers("Content-Type: application/json")
    List<BankAccountPivot> getPivotsByBankAccount(@Param("bankAccountId") String bankAccountId);

    @RequestLine("GET /bank/bank-accounts-pivot/account/{accountId}")
    @Headers("Content-Type: application/json")
    List<BankAccountPivot> getPivotsByAccount(@Param("accountId") String accountId);

    @RequestLine("DELETE /bank/bank-accounts-pivot/bank-account/{bankAccountId}")
    @Headers("Content-Type: application/json")
    void deleteAllPivotsByBankAccount(@Param("bankAccountId") String bankAccountId);

    @RequestLine("DELETE /bank/bank-accounts-pivot/account/{accountId}")
    @Headers("Content-Type: application/json")
    void deleteAllPivotsByAccount(@Param("accountId") String accountId);

    // ==================== CLIENT ====================

    @RequestLine("GET /bank/my-bank-accounts")
    @Headers({"Content-Type: application/json", "X-User-Id: {userId}"})
    List<BankAccount> getMyBankAccounts(@Param("userId") String userId);

    @RequestLine("GET /bank/my-bank-accounts?type_id={typeId}")
    @Headers({"Content-Type: application/json", "X-User-Id: {userId}"})
    List<BankAccount> getMyBankAccountsByType(@Param("userId") String userId, @Param("typeId") Integer typeId);

    @RequestLine("GET /bank/my-bank-accounts/{id}")
    @Headers({"Content-Type: application/json", "X-User-Id: {userId}"})
    BankAccountDetail getMyBankAccountById(@Param("userId") String userId, @Param("id") String id);

    @RequestLine("GET /bank/my-bank-accounts/{id}/co-holders")
    @Headers({"Content-Type: application/json", "X-User-Id: {userId}"})
    List<String> getMyCoHolders(@Param("userId") String userId, @Param("id") String id);
}