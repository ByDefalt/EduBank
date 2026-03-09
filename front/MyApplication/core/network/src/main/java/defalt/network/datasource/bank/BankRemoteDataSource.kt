package defalt.network.datasource.bank

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.network.api.bank.model.BankAccountsPostRequest
import defalt.network.api.bank.service.BankAccountApi
import defalt.network.api.bank.service.BankAccountParameterApi
import defalt.network.mapper.bank.toEntity
import defalt.network.mapper.bank.toBankAccountEntity
import defalt.network.mapper.bank.toParametersIdPutRequestDto
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class BankRemoteDataSource(
    private val bankAccountApi: BankAccountApi,
    private val bankAccountParameterApi: BankAccountParameterApi,
) : IBankRemoteDataSource {

    // --- ADMIN ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankAccountsGet(accountId = accountId) }
            .map { it.toBankAccountEntity() }

    override suspend fun adminCreateBankAccount(
        accountId: Int,
        request: BankAccountCreateRequest,
    ): NetworkResult<BankAccountDetail> {
        val body = BankAccountsPostRequest(
            parameterId = 0,
            typeId = request.typeId,
            sold = request.sold,
            iban = request.iban,
            accountId = accountId,
        )
        // bankAccountsPost retourne BankAccount, on recharge ensuite le détail
        val createResult = safeApiCall { bankAccountApi.bankAccountsPost(body) }
        if (createResult is NetworkResult.Error) return NetworkResult.Error(createResult.code, createResult.message)
        if (createResult is NetworkResult.Exception) return NetworkResult.Exception(createResult.throwable)
        val createdId = (createResult as NetworkResult.Success).data.id
        return safeApiCall { bankAccountApi.bankAccountsIdGet(createdId) }
            .map { it.toEntity() }
    }

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankAccountsGet() }
            .map { it.toBankAccountEntity() }

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        safeApiCall { bankAccountApi.bankAccountsIdDelete(id.toIntOrNull() ?: 0) }

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.bankAccountsIdGet(id.toIntOrNull() ?: 0) }
            .map { it.toEntity() }

    override suspend fun adminUpdateBankAccountParameters(
        bankAccountId: String,
        parameter: BankAccountParameter,
    ): NetworkResult<Unit> {
        val paramId = parameter.id ?: return NetworkResult.Error(400, "Parameter id manquant")
        return safeApiCall {
            bankAccountParameterApi.parametersIdPut(paramId, parameter.toParametersIdPutRequestDto())
        }.map { }
    }

    override suspend fun adminUpdateBankAccount(
        bankAccountId: String,
        typeId: Int,
        parameter: BankAccountParameter,
    ): NetworkResult<BankAccountDetail> {
        // Met à jour les paramètres
        val paramId = parameter.id ?: return NetworkResult.Error(400, "Parameter id manquant")
        val paramResult = safeApiCall {
            bankAccountParameterApi.parametersIdPut(paramId, parameter.toParametersIdPutRequestDto())
        }
        if (paramResult is NetworkResult.Error) return NetworkResult.Error(paramResult.code, paramResult.message)
        if (paramResult is NetworkResult.Exception) return NetworkResult.Exception(paramResult.throwable)
        // Recharge le détail mis à jour
        return safeApiCall { bankAccountApi.bankAccountsIdGet(bankAccountId.toIntOrNull() ?: 0) }
            .map { it.toEntity() }
    }

    // --- CLIENT ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankAccountsGet(typeId = typeId) }
            .map { it.toBankAccountEntity() }

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.bankAccountsIdGet(id.toIntOrNull() ?: 0) }
            .map { it.toEntity() }

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>> =
        NetworkResult.Success(emptyList())
}
