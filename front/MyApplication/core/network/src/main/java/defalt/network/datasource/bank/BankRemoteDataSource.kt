package defalt.network.datasource.bank

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.*
import defalt.network.api.bank.service.*
import defalt.network.mapper.bank.toDto
import defalt.network.mapper.bank.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class BankRemoteDataSource(
    private val bankAccountApi: BankAccountApi,
    private val bankAccountParameterApi: BankAccountParameterApi,
    private val bankAccountPivotApi: BankAccountPivotApi,
    private val typeApi: TypeApi
) : IBankRemoteDataSource {

    // --- ADMIN : Comptes bancaires ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: String): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankAdminAccountsAccountIdBankAccountsGet(accountId) }.map { it.toEntity() }

    override suspend fun adminCreateBankAccount(
        accountId: String,
        request: BankAccountCreateRequest
    ): NetworkResult<BankAccountDetail> =
        safeApiCall {
            bankAccountApi.bankAdminAccountsAccountIdBankAccountsPost(
                accountId,
                request.toDto()
            )
        }.map { it.toEntity() }

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankAdminBankAccountsGet() }.map { it.toEntity() }

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        safeApiCall { bankAccountApi.bankAdminBankAccountsIdDelete(id) }

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.bankAdminBankAccountsIdGet(id) }.map { it.toEntity() }

    override suspend fun adminUpdateBankAccountParameters(
        bankAccountId: String,
        parameter: BankAccountParameter
    ): NetworkResult<Unit> =
        safeApiCall {
            bankAccountParameterApi.bankAdminBankAccountsBankAccountIdParametersPatch(
                bankAccountId,
                parameter.toDto()
            )
        }

    // --- ADMIN : Types ---

    override suspend fun adminGetAllTypes(): NetworkResult<List<Type>> =
        safeApiCall { typeApi.bankAdminTypesGet() }.map { it.toEntity() }

    override suspend fun adminGetTypeById(id: Int): NetworkResult<Type> =
        safeApiCall { typeApi.bankAdminTypesIdGet(id) }.map { it.toEntity() }

    override suspend fun adminCreateType(type: Type): NetworkResult<Type> =
        safeApiCall { typeApi.bankAdminTypesPost(type.toDto()) }.map { it.toEntity() }

    // --- ADMIN : Co-titulaires (Pivot) ---

    override suspend fun adminAddCoHolder(pivot: BankAccountPivot): NetworkResult<Unit> =
        safeApiCall { bankAccountPivotApi.bankBankAccountsPivotPost(pivot.toDto()) }

    override suspend fun adminRemoveCoHolder(pivot: BankAccountPivot): NetworkResult<Unit> =
        safeApiCall { bankAccountPivotApi.bankBankAccountsPivotDelete(pivot.toDto()) }

    override suspend fun adminRemoveAllCoHoldersByBankAccount(bankAccountId: String): NetworkResult<Unit> =
        safeApiCall {
            bankAccountPivotApi.bankBankAccountsPivotBankAccountBankAccountIdDelete(
                bankAccountId
            )
        }

    override suspend fun adminRemoveAllBankAccountsByAccount(accountId: String): NetworkResult<Unit> =
        safeApiCall { bankAccountPivotApi.bankBankAccountsPivotAccountAccountIdDelete(accountId) }

    override suspend fun adminGetCoHoldersByBankAccount(bankAccountId: String): NetworkResult<List<BankAccountPivot>> =
        safeApiCall {
            bankAccountPivotApi.bankBankAccountsPivotBankAccountBankAccountIdGet(
                bankAccountId
            )
        }.map { it.toEntity() }

    // --- CLIENT : Mes Comptes ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> =
        safeApiCall { bankAccountApi.bankMyBankAccountsGet(typeId) }.map { it.toEntity() }

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        safeApiCall { bankAccountApi.bankMyBankAccountsIdGet(id) }.map { it.toEntity() }

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<String>> =
        safeApiCall { bankAccountApi.bankMyBankAccountsIdCoHoldersGet(id) }

    override suspend fun getMyPivotsByAccountId(accountId: String): NetworkResult<List<BankAccountPivot>> =
        safeApiCall { bankAccountPivotApi.bankBankAccountsPivotAccountAccountIdGet(accountId) }.map { it.toEntity() }

}