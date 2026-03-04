package defalt.network.api.datasource

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.network.api.account.service.AccountApi
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class AccountRemoteDataSource(
    private val api: AccountApi,
) : IAccountRemoteDataSource {
    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountById(id: String): NetworkResult<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(id: Int): NetworkResult<Unit> {
        TODO("Not yet implemented")
    }
}
