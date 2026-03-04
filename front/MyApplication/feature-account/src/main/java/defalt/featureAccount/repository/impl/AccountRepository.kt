package defalt.featureAccount.repository.impl

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.AccountRegisterEntity
import defalt.featureAccount.repository.service.IAccountRepository

class AccountRepository(
    private val remoteDataSource: IAccountRemoteDataSource,
) : IAccountRepository {

    override suspend fun getAccounts() = remoteDataSource.getAccounts()
    override suspend fun getAccountById(id: String) = remoteDataSource.getAccountById(id)
    override suspend fun createAccount(accountRegister: AccountRegisterEntity) =
        remoteDataSource.createAccount(accountRegister)

    override suspend fun deleteAccount(id: Int) = remoteDataSource.deleteAccount(id)
}
