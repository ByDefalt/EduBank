package defalt.network.datasource

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.entity.AccountRegisterEntity
import defalt.network.api.account.mapper.toDomain
import defalt.network.api.account.mapper.toDto
import defalt.network.api.account.service.AccountApi
import defalt.network.utils.safeApiCall
import defalt.utils.map

class AccountRemoteDataSource(
    private val api: AccountApi,
) : IAccountRemoteDataSource {

    override suspend fun getAccounts() = safeApiCall { api.accountsGet() }
        .map { list -> list.map { it.toDomain() } }

    override suspend fun getAccountById(id: String) = safeApiCall { api.accountsIdGet(id) }
        .map { it.toDomain() }

    override suspend fun createAccount(accountRegister: AccountRegisterEntity) = safeApiCall {
        api.accountsPost(accountRegister.toDto())
    }.map { it.toDomain() }

    override suspend fun deleteAccount(id: Int) = safeApiCall { api.accountsIdDelete(id) }
}
