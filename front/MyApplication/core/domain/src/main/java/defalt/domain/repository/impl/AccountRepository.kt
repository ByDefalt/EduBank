package defalt.domain.repository.impl

import defalt.domain.datasource.account.IAccountLocalDataSource
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
    private val localDataSource: IAccountLocalDataSource,
) : IAccountRepository {

    // --- COMPTES ---

    override suspend fun getAccounts(): NetworkResult<List<Account>> =
        remoteDataSource.getAccounts()

    override suspend fun getAccountById(id: String): NetworkResult<Account> =
        remoteDataSource.getAccountById(id)

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> =
        remoteDataSource.createAccount(accountRegister)

    override suspend fun activateAccount(id: String): NetworkResult<Boolean> =
        remoteDataSource.activateAccount(id)

    override suspend fun deactivateAccount(id: String): NetworkResult<Boolean> =
        remoteDataSource.deactivateAccount(id)

    override suspend fun closeAccount(id: String): NetworkResult<Boolean> =
        remoteDataSource.closeAccount(id)

    // --- AUTHENTIFICATION ---

    override suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest> =
        remoteDataSource.signIn(signInRequest).also { result ->
            if (result is NetworkResult.Success) {
                localDataSource.registerToken(result.data)
            }
        }

    override suspend fun signOut(): NetworkResult<Boolean> =
        localDataSource.unregisterToken(TokenRequest(jwt = ""))

    override suspend fun getSavedToken(): NetworkResult<TokenRequest> =
        localDataSource.getToken()

    override suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse> =
        remoteDataSource.validateToken(tokenRequest)

    // --- RÔLES ---

    override suspend fun getRoles(): NetworkResult<List<Role>> =
        remoteDataSource.getRoles()

    override suspend fun getRoleById(id: Int): NetworkResult<Role> =
        remoteDataSource.getRoleById(id)

    override suspend fun getRoleByName(name: String): NetworkResult<Role> =
        remoteDataSource.getRoleByName(name)

    override suspend fun getAccountRole(accountId: String): NetworkResult<Role> =
        remoteDataSource.getAccountRole(accountId)

    // --- INFORMATIONS PERSONNELLES ---

    override suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>> =
        remoteDataSource.getPersonalInformations()

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> =
        remoteDataSource.getPersonalInformationById(id)

    override suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation> =
        remoteDataSource.getPersonalInformationByAccountId(accountId)

    override suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation> =
        remoteDataSource.createPersonalInformation(personalInformationRegister)

    override suspend fun updatePersonalInformation(id: Int, personalInformation: PersonalInformation): NetworkResult<PersonalInformation> =
        remoteDataSource.updatePersonalInformation(id, personalInformation)

    override fun insertTokenInHeaders(token: String): NetworkResult<Boolean> {
        return remoteDataSource.insertTokenInHeaders(token)
    }
}
