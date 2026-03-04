package defalt.domain.datasource.account

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.utils.NetworkResult

interface IAccountRemoteDataSource {
    suspend fun getAccounts(): NetworkResult<List<Account>>
    suspend fun getAccountById(id: String): NetworkResult<Account>
    suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account>
    suspend fun deleteAccount(id: Int): NetworkResult<Unit>
}
