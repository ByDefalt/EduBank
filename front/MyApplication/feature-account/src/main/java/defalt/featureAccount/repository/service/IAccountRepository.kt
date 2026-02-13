package defalt.featureAccount.repository.service

import defalt.core.api.account.model.Account
import defalt.core.api.account.model.AccountRegister
import defalt.core.utils.NetworkResult

interface IAccountRepository {
    suspend fun getAccounts(): NetworkResult<List<Account>>
    suspend fun getAccountById(id: String): NetworkResult<Account>
    suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account>
    suspend fun deleteAccount(id: Int): NetworkResult<Unit>
}
