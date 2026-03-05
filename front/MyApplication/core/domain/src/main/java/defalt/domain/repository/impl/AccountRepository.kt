package defalt.domain.repository.impl

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.domain.repository.service.IAccountRepository
import defalt.utils.NetworkResult

class AccountRepository(
    private val remoteDataSource: IAccountRemoteDataSource,
) : IAccountRepository {

    // --- COMPTES ---

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return remoteDataSource.getAccounts()
    }

    override suspend fun getAccountById(id: String): NetworkResult<Account> {
        return remoteDataSource.getAccountById(id)
    }

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> {
        return remoteDataSource.createAccount(accountRegister)
    }

    override suspend fun deleteAccount(id: String): NetworkResult<Boolean> {
        return remoteDataSource.deleteAccount(id)
    }

    override suspend fun activateAccount(id: String): NetworkResult<Boolean> {
        return remoteDataSource.activateAccount(id)
    }

    override suspend fun deactivateAccount(id: String): NetworkResult<Boolean> {
        return remoteDataSource.deactivateAccount(id)
    }

    // --- AUTHENTIFICATION ---

    override suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest> {
        return remoteDataSource.signIn(signInRequest)
    }

    override suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse> {
        return remoteDataSource.validateToken(tokenRequest)
    }

    // --- RÔLES ---

    override suspend fun getRoles(): NetworkResult<List<Role>> {
        return remoteDataSource.getRoles()
    }

    override suspend fun getRoleById(id: Int): NetworkResult<Role> {
        return remoteDataSource.getRoleById(id)
    }

    override suspend fun getAccountRole(accountId: String): NetworkResult<Role> {
        return remoteDataSource.getAccountRole(accountId)
    }

    // --- INFORMATIONS PERSONNELLES ---

    override suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>> {
        return remoteDataSource.getPersonalInformations()
    }

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> {
        return remoteDataSource.getPersonalInformationById(id)
    }

    override suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation> {
        return remoteDataSource.getPersonalInformationByAccountId(accountId)
    }

    override suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation> {
        return remoteDataSource.createPersonalInformation(personalInformationRegister)
    }
}
