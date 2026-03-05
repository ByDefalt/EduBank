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

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun adminCreateBankAccount(accountId: Int, request: BankAccountCreateRequest): NetworkResult<BankAccountDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun adminGetAllBankAccounts(): NetworkResult<List<BankAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun adminDeleteBankAccount(id: String): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun adminGetBankAccountById(id: String): NetworkResult<BankAccountDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun adminUpdateBankAccountParameters(bankAccountId: String, parameter: BankAccountParameter): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

    // --- CLIENT ---

    override suspend fun getMyBankAccounts(typeId: Int?): NetworkResult<List<BankAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyBankAccountById(id: String): NetworkResult<BankAccountDetail> {
        TODO("Not yet implemented")
    }

    override suspend fun getMyBankAccountCoHolders(id: String): NetworkResult<List<Int>> {
        TODO("Not yet implemented")
    }
}
