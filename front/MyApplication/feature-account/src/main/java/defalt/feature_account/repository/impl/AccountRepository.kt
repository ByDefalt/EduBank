package defalt.feature_account.repository.impl

import defalt.core.api.account.model.Account
import defalt.core.api.account.model.AccountRegister
import defalt.core.api.account.service.AccountApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_account.repository.service.IAccountRepository

class AccountRepository(
    private val api: AccountApi
) : IAccountRepository {

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return safeApiCall { api.accountsGet() }
    }

    override suspend fun getAccountById(id: String): NetworkResult<Account> {
        return safeApiCall { api.accountsIdGet(id) }
    }

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> {
        return safeApiCall { api.accountsPost(accountRegister) }
    }

    override suspend fun deleteAccount(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.accountsIdDelete(id) }
    }
}
