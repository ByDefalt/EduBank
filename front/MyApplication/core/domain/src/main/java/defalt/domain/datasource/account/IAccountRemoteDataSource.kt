package defalt.domain.datasource.account

import defalt.domain.entity.account.AccountEntity
import defalt.domain.entity.account.AccountRegisterEntity
import defalt.utils.NetworkResult

interface IAccountRemoteDataSource {
    suspend fun getAccounts(): NetworkResult<List<AccountEntity>>
    suspend fun getAccountById(id: String): NetworkResult<AccountEntity>
    suspend fun createAccount(accountRegister: AccountRegisterEntity): NetworkResult<AccountEntity>
    suspend fun deleteAccount(id: Int): NetworkResult<Unit>
}
