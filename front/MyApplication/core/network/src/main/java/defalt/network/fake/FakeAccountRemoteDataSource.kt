package defalt.network.fake

import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.utils.NetworkResult

class FakeAccountRemoteDataSource : IAccountRemoteDataSource {

    // Copie mutable des données pour simuler les créations / suppressions
    private val accounts = FakeData.accounts.toMutableList()
    private val personalInformations = FakeData.personalInformations.toMutableList()

    // --- COMPTES ---

    override suspend fun getAccounts(): NetworkResult<List<Account>> =
        NetworkResult.Success(accounts.toList())

    override suspend fun getAccountById(id: String): NetworkResult<Account> =
        accounts.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Compte introuvable : $id")

    override suspend fun createAccount(accountRegister: AccountRegister): NetworkResult<Account> {
        val newPersonalInfo = PersonalInformation(
            id          = personalInformations.size + 1,
            firstname   = accountRegister.personalInfo.firstname,
            lastname    = accountRegister.personalInfo.lastname,
            email       = accountRegister.personalInfo.email,
            address     = accountRegister.personalInfo.address,
            phoneNumber = accountRegister.personalInfo.phoneNumber,
        )
        val newAccount = Account(
            id             = "acc-${(accounts.size + 1).toString().padStart(4, '0')}",
            personalInfoId = newPersonalInfo.id,
            roleId         = FakeData.roles.find { it.name == accountRegister.role.value }?.id,
            state          = AccountStateEnum.INACTIVE,
        )
        personalInformations.add(newPersonalInfo)
        accounts.add(newAccount)
        return NetworkResult.Success(newAccount)
    }

    override suspend fun activateAccount(id: String): NetworkResult<Boolean> {
        val index = accounts.indexOfFirst { it.id == id }
        return if (index != -1) {
            accounts[index] = accounts[index].copy(state = AccountStateEnum.ACTIVE)
            NetworkResult.Success(true)
        } else NetworkResult.Error(code = 404, message = "Compte introuvable : $id")
    }

    override suspend fun deactivateAccount(id: String): NetworkResult<Boolean> {
        val index = accounts.indexOfFirst { it.id == id }
        return if (index != -1) {
            accounts[index] = accounts[index].copy(state = AccountStateEnum.INACTIVE)
            NetworkResult.Success(true)
        } else NetworkResult.Error(code = 404, message = "Compte introuvable : $id")
    }

    // --- AUTHENTIFICATION ---

    override suspend fun signIn(signInRequest: SignInRequest): NetworkResult<TokenRequest> {
        val credential = FakeData.credentials.find {
            it.email == signInRequest.id && it.password == signInRequest.password
        }
        return if (credential != null) {
            NetworkResult.Success(TokenRequest(jwt = FakeData.FAKE_JWT))
        } else {
            NetworkResult.Error(code = 401, message = "Identifiants invalides")
        }
    }

    override suspend fun validateToken(tokenRequest: TokenRequest): NetworkResult<TokenResponse> =
        if (tokenRequest.jwt == FakeData.FAKE_JWT) {
            NetworkResult.Success(FakeData.tokenResponseAlice)
        } else {
            NetworkResult.Error(code = 401, message = "Token invalide ou expiré")
        }

    // --- RÔLES ---

    override suspend fun getRoles(): NetworkResult<List<Role>> =
        NetworkResult.Success(FakeData.roles)

    override suspend fun getRoleById(id: Int): NetworkResult<Role> =
        FakeData.roles.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Rôle introuvable : $id")

    override suspend fun getAccountRole(accountId: String): NetworkResult<Role> {
        val account = accounts.find { it.id == accountId }
            ?: return NetworkResult.Error(code = 404, message = "Compte introuvable : $accountId")
        return FakeData.roles.find { it.id == account.roleId }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Rôle introuvable pour le compte : $accountId")
    }

    // --- INFORMATIONS PERSONNELLES ---

    override suspend fun getPersonalInformations(): NetworkResult<List<PersonalInformation>> =
        NetworkResult.Success(personalInformations.toList())

    override suspend fun getPersonalInformationById(id: Int): NetworkResult<PersonalInformation> =
        personalInformations.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Info personnelle introuvable : $id")

    override suspend fun getPersonalInformationByAccountId(accountId: String): NetworkResult<PersonalInformation> {
        val account = accounts.find { it.id == accountId }
            ?: return NetworkResult.Error(code = 404, message = "Compte introuvable : $accountId")
        return personalInformations.find { it.id == account.personalInfoId }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Info personnelle introuvable pour : $accountId")
    }

    override suspend fun createPersonalInformation(
        personalInformationRegister: PersonalInformationRegister,
    ): NetworkResult<PersonalInformation> {
        val newInfo = PersonalInformation(
            id          = personalInformations.size + 1,
            firstname   = personalInformationRegister.firstname,
            lastname    = personalInformationRegister.lastname,
            email       = personalInformationRegister.email,
            address     = personalInformationRegister.address,
            phoneNumber = personalInformationRegister.phoneNumber,
        )
        personalInformations.add(newInfo)
        return NetworkResult.Success(newInfo)
    }
}

