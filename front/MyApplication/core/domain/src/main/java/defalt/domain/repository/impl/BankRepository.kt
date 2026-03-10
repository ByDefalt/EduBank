package defalt.domain.repository.impl

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.BankAccountPivot
import defalt.domain.entity.bank.Type
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult

class BankRepository(
    private val remoteDataSource: IBankRemoteDataSource,
) : IBankRepository {

    // --- ADMIN : Comptes bancaires ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: String): NetworkResult<List<BankAccount>> =
        remoteDataSource.adminGetBankAccountsByAccountId(accountId)

    override suspend fun adminCreateBankAccount(accountId: String, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail> =
        remoteDataSource.adminCreateBankAccount(accountId, request)

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        remoteDataSource.adminGetAllBankAccounts()

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        remoteDataSource.adminDeleteBankAccount(id)

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        remoteDataSource.adminGetBankAccountById(id)

    // --- ADMIN : Paramètres ---

    override suspend fun adminUpdateBankAccountParameters(bankAccountId: String, parameter: BankAccountParameter): NetworkResult<Unit> =
        remoteDataSource.adminUpdateBankAccountParameters(bankAccountId, parameter)

    // --- ADMIN : Types de compte ---

    override suspend fun adminGetAllTypes(): NetworkResult<List<Type>> =
        remoteDataSource.adminGetAllTypes()

    override suspend fun adminGetTypeById(id: Int): NetworkResult<Type> =
        remoteDataSource.adminGetTypeById(id)

    override suspend fun adminCreateType(type: Type): NetworkResult<Type> =
        remoteDataSource.adminCreateType(type)

    // --- ADMIN : Co-titulaires / Pivot ---

    override suspend fun adminAddCoHolder(pivot: BankAccountPivot): NetworkResult<Unit> =
        remoteDataSource.adminAddCoHolder(pivot)

    override suspend fun adminRemoveCoHolder(pivot: BankAccountPivot): NetworkResult<Unit> =
        remoteDataSource.adminRemoveCoHolder(pivot)

    override suspend fun adminRemoveAllCoHoldersByBankAccount(bankAccountId: String): NetworkResult<Unit> =
        remoteDataSource.adminRemoveAllCoHoldersByBankAccount(bankAccountId)

    override suspend fun adminRemoveAllBankAccountsByAccount(accountId: String): NetworkResult<Unit> =
        remoteDataSource.adminRemoveAllBankAccountsByAccount(accountId)

    override suspend fun adminGetCoHoldersByBankAccount(bankAccountId: String): NetworkResult<List<BankAccountPivot>> =
        remoteDataSource.adminGetCoHoldersByBankAccount(bankAccountId)

    // --- CLIENT : Mes Comptes ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> =
        remoteDataSource.getMyBankAccounts(typeId)

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        remoteDataSource.getMyBankAccountById(id)

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<String>> =
        remoteDataSource.getMyBankAccountCoHolders(id)

    override suspend fun getMyPivotsByAccountId(accountId: String): NetworkResult<List<BankAccountPivot>> =
        remoteDataSource.getMyPivotsByAccountId(accountId)
}