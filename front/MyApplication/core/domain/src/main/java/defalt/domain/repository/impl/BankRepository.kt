package defalt.domain.repository.impl

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.repository.service.IBankRepository
import defalt.utils.NetworkResult

class BankRepository(
    private val remoteDataSource: IBankRemoteDataSource,
) : IBankRepository {

    // --- ADMIN ---

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> =
        remoteDataSource.adminGetBankAccountsByAccountId(accountId)

    override suspend fun adminCreateBankAccount(accountId: Int, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail> =
        remoteDataSource.adminCreateBankAccount(accountId, request)

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> =
        remoteDataSource.adminGetAllBankAccounts()

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> =
        remoteDataSource.adminDeleteBankAccount(id)

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        remoteDataSource.adminGetBankAccountById(id)

    override suspend fun adminUpdateBankAccountParameters(bankAccountId: String, parameter: BankAccountParameter): NetworkResult<Unit> =
        remoteDataSource.adminUpdateBankAccountParameters(bankAccountId, parameter)

    // --- CLIENT ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> =
        remoteDataSource.getMyBankAccounts(typeId)

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> =
        remoteDataSource.getMyBankAccountById(id)

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>> =
        remoteDataSource.getMyBankAccountCoHolders(id)
}
