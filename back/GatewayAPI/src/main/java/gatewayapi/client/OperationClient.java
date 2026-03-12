package gatewayapi.client;

import dto.operationapi.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface OperationClient {

    @RequestLine("GET /operations?state={state}&date_from={dateFrom}&date_to={dateTo}")
    @Headers("Content-Type: application/json")
    OperationList getAllOperations(@Param("state") OperationState state, @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

    @RequestLine("GET /operations/account/{accountId}?state={state}&date_from={dateFrom}&date_to={dateTo}")
    @Headers("Content-Type: application/json")
    OperationList getOperationsByAccountId(@Param("accountId") String accountId, @Param("state") OperationState state, @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);

    @RequestLine("POST /operations")
    @Headers("Content-Type: application/json")
    Operation createOperation(Operation operation);

    @RequestLine("GET /operations/{id}")
    @Headers("Content-Type: application/json")
    Operation getOperationById(@Param("id") Integer id);

    @RequestLine("PATCH /operations/{id}/state?state={state}")
    @Headers("Content-Type: application/json")
    Operation updateOperationState(@Param("id") Integer id, @Param("state") OperationState state);

    @RequestLine("POST /operations/{id}/cancel")
    @Headers("Content-Type: application/json")
    Operation cancelOperation(@Param("id") Integer id);

    @RequestLine("GET /beneficiaries")
    @Headers("Content-Type: application/json")
    BeneficiaryList getAllBeneficiaries();

    @RequestLine("GET /beneficiaries/{accountId}")
    @Headers("Content-Type: application/json")
    BeneficiaryList getBeneficiariesByAccountId(@Param("accountId") String accountId);

    @RequestLine("POST /beneficiaries")
    @Headers("Content-Type: application/json")
    Beneficiary createBeneficiary(Beneficiary beneficiary);

    @RequestLine("PUT /beneficiaries/{id}")
    @Headers("Content-Type: application/json")
    Beneficiary updateBeneficiary(@Param("id") Integer id, Beneficiary beneficiary);

    @RequestLine("DELETE /beneficiaries/{id}")
    @Headers("Content-Type: application/json")
    void deleteBeneficiary(@Param("id") Integer id);
}