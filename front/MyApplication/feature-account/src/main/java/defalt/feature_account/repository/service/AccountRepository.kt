package defalt.feature_account.repository.service

import defalt.core.api.account.model.Account
import defalt.core.utils.NetworkResult

interface AccountRepository {
    suspend fun getAccounts(): NetworkResult<List<Account>>
}