package defalt.network.datasource.bank

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.network.api.bank.service.BankAccountApi
import defalt.network.api.bank.service.BankAccountParameterApi
import defalt.utils.NetworkResult

class BankRemoteDataSource(
    private val bankAccountApi: BankAccountApi,
    private val bankAccountParameterApi: BankAccountParameterApi,
) : IBankRemoteDataSource {

    override suspend fun adminGetBankAccountsByAccountId(accountId: Int): NetworkResult<List<BankAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun adminCreateBankAccount(
        accountId: Int,
        request: BankAccountCreateRequest,
    ): NetworkResult<BankAccountDetail> {
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

    override suspend fun adminUpdateBankAccountParameters(
        bankAccountId: String,
        parameter: BankAccountParameter,
    ): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }

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
