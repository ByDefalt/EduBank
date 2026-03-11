package defalt.domain.datasource.account

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.utils.NetworkResult

interface IAccountRemoteDataSource {

    // --- COMPTES (AccountApi) ---

    suspend fun activateAccount(id: String): NetworkResult<Boolean>

    suspend fun deactivateAccount(id: String): NetworkResult<Boolean>

    suspend fun getAccounts(): NetworkResult<List<Account>>

    suspend fun getAccountById(id: String): NetworkResult<Account>

    /** * Clôture un compte (changement d'état à ENCLOSE) via PUT accounts/{id}
     */
    suspend fun closeAccount(id: String): NetworkResult<Boolean>

    suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account>

    // --- AUTHENTIFICATION (AccountApi) ---

    suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest>

    suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse>

    // --- RÔLES (RoleApi & AccountApi) ---

    suspend fun getRoles(): NetworkResult<List<Role>>

    suspend fun getRoleById(id: Int): NetworkResult<Role>

    suspend fun getRoleByName(name: String): NetworkResult<Role>

    /** Récupère le rôle spécifiquement lié à un compte */
    suspend fun getAccountRole(accountId: String): NetworkResult<Role>

    // --- INFORMATIONS PERSONNELLES (PersonalInformationApi & AccountApi) ---

    suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>>

    suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation>

    /** Récupère les infos personnelles liées à un compte spécifique */
    suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation>

    suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation>

    suspend fun updatePersonalInformation(id: Int, personalInformation: PersonalInformation): NetworkResult<PersonalInformation>

    fun insertTokenInHeaders(token: String): NetworkResult<Boolean>
}
