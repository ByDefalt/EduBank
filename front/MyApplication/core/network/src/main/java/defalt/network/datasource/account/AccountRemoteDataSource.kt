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
import defalt.domain.session.Session
import defalt.network.api.account.service.AccountApi
import defalt.network.api.account.service.PersonalInformationApi
import defalt.network.api.account.service.RoleApi
import defalt.network.infrastructure.ApiClient
import defalt.network.mapper.account.toDto
import defalt.network.mapper.account.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class AccountRemoteDataSource(
    private val api: AccountApi,
    private val personalInformationApi: PersonalInformationApi,
    private val roleApi: RoleApi,
    private val apiClient: ApiClient,
    private val session: Session,
) : IAccountRemoteDataSource {

    // --- COMPTES ---

    override suspend fun getAccounts(): NetworkResult<List<Account>> =
        safeApiCall { api.accountsGet() }.map { it.toEntity() }

    override suspend fun getAccountById(id: String): NetworkResult<Account> =
        safeApiCall { api.accountsIdGet(id) }.map { it.toEntity() }

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> =
        safeApiCall { api.accountsPost(accountRegister.toDto()) }.map { it.toEntity() }

    override suspend fun activateAccount(id: String): NetworkResult<Boolean> =
        safeApiCall { api.accountsActivateIdPut(id) }

    override suspend fun deactivateAccount(id: String): NetworkResult<Boolean> =
        safeApiCall { api.accountsDeactivateIdPut(id) }

    // --- AUTHENTIFICATION ---

    override suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest> {
        val result = safeApiCall { api.accountsSigninPost(signInRequest.toDto()) }.map { it.toEntity() }
        if (result is NetworkResult.Success) {
            val token = result.data.jwt
            apiClient.addAuthorization(
                "Bearer",
            ) { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            session.token = token
            // Récupérer le vrai id et le rôle depuis le token
            val validateResult = safeApiCall { api.accountsValidatePost(result.data.toDto()) }.map { it.toEntity() }
            if (validateResult is NetworkResult.Success) {
                session.accountId = validateResult.data.id
                session.role = validateResult.data.role
            }
        }
        return result
    }

    override suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse> =
        safeApiCall { api.accountsValidatePost(tokenRequest.toDto()) }.map { it.toEntity() }

    // --- RÔLES ---

    override suspend fun getRoles(): NetworkResult<List<Role>> =
        safeApiCall { roleApi.rolesGet() }.map { it.toEntity() }

    override suspend fun getRoleById(id: Int): NetworkResult<Role> =
        safeApiCall { roleApi.rolesIdGet(id) }.map { it.toEntity() }

    override suspend fun getAccountRole(accountId: String): NetworkResult<Role> =
        safeApiCall { api.accountsRoleIdGet(accountId) }.map { it.toEntity() }

    // --- INFORMATIONS PERSONNELLES ---

    override suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>> =
        safeApiCall { personalInformationApi.personalInformationGet() }.map { it.toEntity() }

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> =
        safeApiCall { personalInformationApi.personalInformationIdGet(id) }.map { it.toEntity() }

    override suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation> =
        safeApiCall { api.accountsPersonalInformationIdGet(accountId) }.map { it.toEntity() }

    override suspend fun createPersonalInformation(
        personalInformationRegister: PersonalInformationRegister,
    ): NetworkResult<PersonalInformation> =
        safeApiCall {
            personalInformationApi.personalInformationPost(personalInformationRegister.toDto())
        }.map { it.toEntity() }
}
