package defalt.domain.repository.impl

import defalt.domain.datasource.account.IAccountLocalDataSource
import defalt.domain.datasource.account.IAccountRemoteDataSource
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.Role
import defalt.domain.entity.account.RoleEnum
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccountRepositoryTest {

    private val remote: IAccountRemoteDataSource = mockk()
    private val local: IAccountLocalDataSource = mockk()
    private lateinit var repository: AccountRepository

    private val fakeAccount = Account(id = "acc-001", personalInfoId = 1, roleId = 2, state = AccountStateEnum.ACTIVE)
    private val fakeRole = Role(id = 2, name = "CUSTOMER")
    private val fakePersonalInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")
    private val fakeToken = TokenRequest(jwt = "jwt-token")
    private val fakeTokenResponse = TokenResponse(id = "acc-001", role = "CUSTOMER")

    @Before fun setUp() { repository = AccountRepository(remote, local) }



    @Test fun `getAccounts delegue au remote`() = runTest {
        coEvery { remote.getAccounts() } returns NetworkResult.Success(listOf(fakeAccount))
        val result = repository.getAccounts()
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { remote.getAccounts() }
    }

    @Test fun `getAccounts propage Error`() = runTest {
        coEvery { remote.getAccounts() } returns NetworkResult.Error(401, "unauthorized")
        assertTrue(repository.getAccounts() is NetworkResult.Error)
    }



    @Test fun `getAccountById delegue l id`() = runTest {
        coEvery { remote.getAccountById("acc-001") } returns NetworkResult.Success(fakeAccount)
        val result = repository.getAccountById("acc-001")
        assertTrue(result is NetworkResult.Success)
        assertEquals("acc-001", (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { remote.getAccountById("acc-001") }
    }

    @Test fun `getAccountById propage Error 404`() = runTest {
        coEvery { remote.getAccountById(any()) } returns NetworkResult.Error(404, "not found")
        assertTrue(repository.getAccountById("acc-001") is NetworkResult.Error)
    }



    @Test fun `createAccount delegue`() = runTest {
        val req = AccountRegister(
            personalInfo = PersonalInformationRegister(
                firstname = "Alice",
                lastname = "Dupont",
                email = "alice@mail.fr",
                address = "1 rue",
                phoneNumber = "06",
            ),
            role = RoleEnum.CUSTOMER,
            password = "pass",
        )
        coEvery { remote.createAccount(req) } returns NetworkResult.Success(fakeAccount)
        val result = repository.createAccount(req)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.createAccount(req) }
    }



    @Test fun `activateAccount delegue`() = runTest {
        coEvery { remote.activateAccount("acc-001") } returns NetworkResult.Success(true)
        assertTrue((repository.activateAccount("acc-001") as NetworkResult.Success).data)
        coVerify(exactly = 1) { remote.activateAccount("acc-001") }
    }

    @Test fun `deactivateAccount delegue`() = runTest {
        coEvery { remote.deactivateAccount("acc-001") } returns NetworkResult.Success(true)
        assertTrue((repository.deactivateAccount("acc-001") as NetworkResult.Success).data)
        coVerify(exactly = 1) { remote.deactivateAccount("acc-001") }
    }



    @Test fun `signIn delegue et stocke le token localement`() = runTest {
        coEvery { remote.signIn(any()) } returns NetworkResult.Success(fakeToken)
        coEvery { local.registerToken(fakeToken) } returns NetworkResult.Success(true)

        val result = repository.signIn(SignInRequest(id = "alice@mail.fr", password = "pass"))

        assertTrue(result is NetworkResult.Success)
        assertEquals("jwt-token", (result as NetworkResult.Success).data.jwt)
        coVerify(exactly = 1) { remote.signIn(any()) }
        coVerify(exactly = 1) { local.registerToken(fakeToken) }
    }

    @Test fun `signIn ne stocke pas si remote echoue`() = runTest {
        coEvery { remote.signIn(any()) } returns NetworkResult.Error(401, "unauthorized")

        val result = repository.signIn(SignInRequest(id = "alice@mail.fr", password = "wrong"))

        assertTrue(result is NetworkResult.Error)
        coVerify(exactly = 0) { local.registerToken(any()) }
    }



    @Test fun `signOut delegue au local`() = runTest {
        coEvery { local.unregisterToken(any()) } returns NetworkResult.Success(true)
        assertTrue((repository.signOut() as NetworkResult.Success).data)
        coVerify(exactly = 1) { local.unregisterToken(any()) }
    }



    @Test fun `getSavedToken delegue au local`() = runTest {
        coEvery { local.getToken() } returns NetworkResult.Success(fakeToken)
        val result = repository.getSavedToken()
        assertTrue(result is NetworkResult.Success)
        assertEquals("jwt-token", (result as NetworkResult.Success).data.jwt)
    }

    @Test fun `getSavedToken propage Error si pas de token`() = runTest {
        coEvery { local.getToken() } returns NetworkResult.Error(404, "no token")
        assertTrue(repository.getSavedToken() is NetworkResult.Error)
    }



    @Test fun `validateToken delegue au remote`() = runTest {
        coEvery { remote.validateToken(fakeToken) } returns NetworkResult.Success(fakeTokenResponse)
        val result = repository.validateToken(fakeToken)
        assertTrue(result is NetworkResult.Success)
        assertEquals("acc-001", (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { remote.validateToken(fakeToken) }
    }



    @Test fun `getRoles delegue`() = runTest {
        coEvery { remote.getRoles() } returns NetworkResult.Success(listOf(fakeRole))
        val result = repository.getRoles()
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
    }

    @Test fun `getRoleById delegue l id`() = runTest {
        coEvery { remote.getRoleById(2) } returns NetworkResult.Success(fakeRole)
        val result = repository.getRoleById(2)
        assertTrue(result is NetworkResult.Success)
        assertEquals("CUSTOMER", (result as NetworkResult.Success).data.name)
        coVerify(exactly = 1) { remote.getRoleById(2) }
    }

    @Test fun `getAccountRole delegue l accountId`() = runTest {
        coEvery { remote.getAccountRole("acc-001") } returns NetworkResult.Success(fakeRole)
        val result = repository.getAccountRole("acc-001")
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.getAccountRole("acc-001") }
    }



    @Test fun `getPersonalInformations delegue`() = runTest {
        coEvery { remote.getPersonalInformations() } returns NetworkResult.Success(listOf(fakePersonalInfo))
        val result = repository.getPersonalInformations()
        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", (result as NetworkResult.Success).data[0].firstname)
    }

    @Test fun `getPersonalInformationById delegue l id`() = runTest {
        coEvery { remote.getPersonalInformationById(1) } returns NetworkResult.Success(fakePersonalInfo)
        val result = repository.getPersonalInformationById(1)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.getPersonalInformationById(1) }
    }

    @Test fun `getPersonalInformationByAccountId delegue`() = runTest {
        coEvery { remote.getPersonalInformationByAccountId("acc-001") } returns NetworkResult.Success(fakePersonalInfo)
        val result = repository.getPersonalInformationByAccountId("acc-001")
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.getPersonalInformationByAccountId("acc-001") }
    }

    @Test fun `createPersonalInformation delegue`() = runTest {
        val req = PersonalInformationRegister(
            firstname = "Alice",
            lastname = "Dupont",
            email = "alice@mail.fr",
            address = "1 rue",
            phoneNumber = "06",
        )
        coEvery { remote.createPersonalInformation(req) } returns NetworkResult.Success(fakePersonalInfo)
        val result = repository.createPersonalInformation(req)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.createPersonalInformation(req) }
    }

    @Test fun `updatePersonalInformation delegue id et entite`() = runTest {
        coEvery { remote.updatePersonalInformation(1, fakePersonalInfo) } returns NetworkResult.Success(fakePersonalInfo)
        val result = repository.updatePersonalInformation(1, fakePersonalInfo)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { remote.updatePersonalInformation(1, fakePersonalInfo) }
    }
}
