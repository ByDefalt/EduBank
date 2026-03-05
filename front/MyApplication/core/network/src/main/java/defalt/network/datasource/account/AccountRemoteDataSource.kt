package defalt.network.datasource.account

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.network.api.account.service.AccountApi
import defalt.network.mapper.account.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class AccountRemoteDataSource(
    private val api: AccountApi,
) : IAccountRemoteDataSource {
    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return safeApiCall { api.accountsGet() }.map { it.toEntity() }
    }

    override suspend fun getAccountById(id: String): NetworkResult<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(id: String): NetworkResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun activateAccount(id: String): NetworkResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deactivateAccount(id: String): NetworkResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest> {
        TODO("Not yet implemented")
    }

    override suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoles(): NetworkResult<List<Role>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoleById(id: Int): NetworkResult<Role> {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountRole(accountId: String): NetworkResult<Role> {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation> {
        TODO("Not yet implemented")
    }

    override suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation> {
        TODO("Not yet implemented")
    }

}