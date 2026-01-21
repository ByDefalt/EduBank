package defalt.feature_account.repository.impl

import defalt.core.api.account.model.Account
import defalt.core.api.account.service.AccountApi
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.feature_account.repository.service.AccountRepository

class AccountRepositoryImpl(
    private val api: AccountApi
) : AccountRepository {

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return safeApiCall { api.accountsGet() }
    }
}