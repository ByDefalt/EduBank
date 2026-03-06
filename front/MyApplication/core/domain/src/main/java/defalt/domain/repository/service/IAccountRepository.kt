package defalt.domain.repository.service

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.utils.NetworkResult

interface IAccountRepository {
    // --- COMPTES ---
    suspend fun getAccounts(): NetworkResult<List<Account>>
    suspend fun getAccountById(id: String): NetworkResult<Account>
    suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account>
    suspend fun activateAccount(id: String): NetworkResult<Boolean>
    suspend fun deactivateAccount(id: String): NetworkResult<Boolean>

    // --- AUTHENTIFICATION ---
    suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest>
    suspend fun signOut(): NetworkResult<Boolean>
    suspend fun getSavedToken(): NetworkResult<TokenRequest>
    suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse>

    // --- RÔLES ---
    suspend fun getRoles(): NetworkResult<List<Role>>
    suspend fun getRoleById(id: Int): NetworkResult<Role>
    suspend fun getAccountRole(accountId: String): NetworkResult<Role>

    // --- INFORMATIONS PERSONNELLES ---
    suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>>
    suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation>
    suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation>
    suspend fun createPersonalInformation(personalInformationRegister: PersonalInformationRegister): NetworkResult<PersonalInformation>
}
