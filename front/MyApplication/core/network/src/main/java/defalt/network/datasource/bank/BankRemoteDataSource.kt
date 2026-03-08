package defalt.network.datasource.bank

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.network.api.bank.service.BankAccountApi
import defalt.network.api.bank.service.BankAccountParameterApi
import defalt.network.mapper.bank.toDto
import defalt.network.mapper.bank.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class BankRemoteDataSource(
    private val bankAccountApi: BankAccountApi,
    private val bankAccountParameterApi: BankAccountParameterApi,
) : IBankRemoteDataSource {

    // --- ADMIN ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.adminAccountsAccountIdBankAccountsGet(accountId) }
            .map { it.toEntity() }

    override suspend fun adminCreateBankAccount(
        accountId: Int,
        request: BankAccountCreateRequest,
    ): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.adminAccountsAccountIdBankAccountsPost(accountId, request.toDto()) }
            .map { it.toEntity() }

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.adminBankAccountsGet() }
            .map { it.toEntity() }

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        safeApiCall { bankAccountApi.adminBankAccountsIdDelete(id) }

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.adminBankAccountsIdGet(id) }
            .map { it.toEntity() }

    override suspend fun adminUpdateBankAccountParameters(
        bankAccountId: String,
        parameter: BankAccountParameter,
    ): NetworkResult<Unit> =
        safeApiCall {
            bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch(bankAccountId, parameter.toDto())
        }

    override suspend fun adminUpdateBankAccount(
        bankAccountId: String,
        typeId: Int,
        parameter: BankAccountParameter,
    ): NetworkResult<BankAccountDetail> {
        // Met à jour les paramètres (overdraft + état)
        val paramResult = safeApiCall {
            bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch(bankAccountId, parameter.toDto())
        }
        if (paramResult is NetworkResult.Error) return NetworkResult.Error(paramResult.code, paramResult.message)
        if (paramResult is NetworkResult.Exception) return NetworkResult.Exception(paramResult.throwable)
        // Recharge le détail mis à jour
        return safeApiCall { bankAccountApi.adminBankAccountsIdGet(bankAccountId) }.map { it.toEntity() }
    }

    // --- CLIENT ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.myBankAccountsGet(typeId) }
            .map { it.toEntity() }

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.myBankAccountsIdGet(id) }
            .map { it.toEntity() }

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>> =
        safeApiCall { bankAccountApi.myBankAccountsIdCoHoldersGet(id) }
}
