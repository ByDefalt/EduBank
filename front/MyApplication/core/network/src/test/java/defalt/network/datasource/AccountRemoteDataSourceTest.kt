package defalt.network.datasource.account

import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.account.PersonalInformationRegister
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.session.Session
import defalt.network.api.account.model.Account as AccountDto
import defalt.network.api.account.model.AccountStateEnum as AccountStateEnumDto
import defalt.network.api.account.model.PersonalInformation as PersonalInformationDto
import defalt.network.api.account.model.Role as RoleDto
import defalt.network.api.account.model.TokenRequest as TokenRequestDto
import defalt.network.api.account.model.TokenResponse as TokenResponseDto
import defalt.network.api.account.service.AccountApi
import defalt.network.api.account.service.PersonalInformationApi
import defalt.network.api.account.service.RoleApi
import defalt.network.infrastructure.ApiClient
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AccountRemoteDataSourceTest {

    private val accountApi: AccountApi = mockk()
    private val personalInformationApi: PersonalInformationApi = mockk()
    private val roleApi: RoleApi = mockk()
    private val apiClient: ApiClient = mockk(relaxed = true)
    private val session = Session()
    private lateinit var dataSource: AccountRemoteDataSource

    private val fakeAccountDto = AccountDto(id = "acc-001", personalInfoId = 1, roleId = 2, state = AccountStateEnumDto.ACTIVE)
    private val fakePersonalInfoDto = PersonalInformationDto(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")
    private val fakeRoleDto = RoleDto(id = 2, name = "CUSTOMER")
    private val fakeTokenResponseDto = TokenResponseDto(id = "acc-001", role = "CUSTOMER")
    private val fakeTokenRequestDto = TokenRequestDto(jwt = "jwt-token")

    @Before fun setUp() {
        dataSource = AccountRemoteDataSource(accountApi, personalInformationApi, roleApi, apiClient, session)
    }

    // ── getAccounts ──────────────────────────────────────────────────────────

    @Test fun `getAccounts retourne la liste en succes`() = runTest {
        coEvery { accountApi.accountsGet() } returns Response.success(listOf(fakeAccountDto))

        val result = dataSource.getAccounts()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("acc-001", result.data[0].id)
    }

    @Test fun `getAccounts retourne liste vide`() = runTest {
        coEvery { accountApi.accountsGet() } returns Response.success(emptyList())

        val result = dataSource.getAccounts()

        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `getAccounts propage Error 401`() = runTest {
        coEvery { accountApi.accountsGet() } returns Response.error(401, "unauthorized".toResponseBody())
        assertTrue(dataSource.getAccounts() is NetworkResult.Error)
    }

    @Test fun `getAccounts propage Exception`() = runTest {
        coEvery { accountApi.accountsGet() } throws RuntimeException("crash")
        assertTrue(dataSource.getAccounts() is NetworkResult.Exception)
    }

    // ── getAccountById ────────────────────────────────────────────────────────

    @Test fun `getAccountById retourne le compte en succes`() = runTest {
        coEvery { accountApi.accountsIdGet("acc-001") } returns Response.success(fakeAccountDto)

        val result = dataSource.getAccountById("acc-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("acc-001", (result as NetworkResult.Success).data.id)
        assertEquals(AccountStateEnum.ACTIVE, result.data.state)
    }

    @Test fun `getAccountById propage Error 404`() = runTest {
        coEvery { accountApi.accountsIdGet("acc-001") } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.getAccountById("acc-001") is NetworkResult.Error)
    }

    @Test fun `getAccountById propage Exception`() = runTest {
        coEvery { accountApi.accountsIdGet(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.getAccountById("acc-001") is NetworkResult.Exception)
    }

    // ── activateAccount ──────────────────────────────────────────────────────

    @Test fun `activateAccount retourne true en succes`() = runTest {
        coEvery { accountApi.accountsActivateIdPut("acc-001") } returns Response.success(true)

        val result = dataSource.activateAccount("acc-001")

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data)
        coVerify(exactly = 1) { accountApi.accountsActivateIdPut("acc-001") }
    }

    @Test fun `activateAccount propage Error`() = runTest {
        coEvery { accountApi.accountsActivateIdPut(any()) } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.activateAccount("acc-001") is NetworkResult.Error)
    }

    // ── deactivateAccount ────────────────────────────────────────────────────

    @Test fun `deactivateAccount retourne true en succes`() = runTest {
        coEvery { accountApi.accountsDeactivateIdPut("acc-001") } returns Response.success(true)

        val result = dataSource.deactivateAccount("acc-001")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { accountApi.accountsDeactivateIdPut("acc-001") }
    }

    @Test fun `deactivateAccount propage Exception`() = runTest {
        coEvery { accountApi.accountsDeactivateIdPut(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.deactivateAccount("acc-001") is NetworkResult.Exception)
    }

    // ── signIn ────────────────────────────────────────────────────────────────

    @Test fun `signIn stocke le token et l accountId dans la session`() = runTest {
        every { apiClient.addAuthorization(any(), any()) } returns apiClient
        coEvery { accountApi.accountsSigninPost(any()) } returns Response.success(fakeTokenRequestDto)
        coEvery { accountApi.accountsValidatePost(any()) } returns Response.success(fakeTokenResponseDto)

        val result = dataSource.signIn(SignInRequest(id = "alice@mail.fr", password = "pass"))

        assertTrue(result is NetworkResult.Success)
        assertEquals("jwt-token", session.token)
        assertEquals("acc-001", session.accountId)
        assertEquals("CUSTOMER", session.role)
    }

    @Test fun `signIn propage Error 401`() = runTest {
        coEvery { accountApi.accountsSigninPost(any()) } returns Response.error(401, "unauthorized".toResponseBody())

        val result = dataSource.signIn(SignInRequest(id = "alice@mail.fr", password = "wrong"))

        assertTrue(result is NetworkResult.Error)
        assertNull(session.token)
    }

    @Test fun `signIn propage Exception`() = runTest {
        coEvery { accountApi.accountsSigninPost(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.signIn(SignInRequest(id = "a@a.fr", password = "p")) is NetworkResult.Exception)
    }

    // ── validateToken ─────────────────────────────────────────────────────────

    @Test fun `validateToken retourne le TokenResponse`() = runTest {
        coEvery { accountApi.accountsValidatePost(any()) } returns Response.success(fakeTokenResponseDto)

        val result = dataSource.validateToken(TokenRequest(jwt = "jwt-token"))

        assertTrue(result is NetworkResult.Success)
        assertEquals("acc-001", (result as NetworkResult.Success).data.id)
        assertEquals("CUSTOMER", result.data.role)
    }

    @Test fun `validateToken propage Error 401`() = runTest {
        coEvery { accountApi.accountsValidatePost(any()) } returns Response.error(401, "expired".toResponseBody())
        assertTrue(dataSource.validateToken(TokenRequest(jwt = "expired")) is NetworkResult.Error)
    }

    // ── getRoles ─────────────────────────────────────────────────────────────

    @Test fun `getRoles retourne la liste en succes`() = runTest {
        coEvery { roleApi.rolesGet() } returns Response.success(listOf(fakeRoleDto))

        val result = dataSource.getRoles()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("CUSTOMER", result.data[0].name)
    }

    @Test fun `getRoles propage Error`() = runTest {
        coEvery { roleApi.rolesGet() } returns Response.error(500, "error".toResponseBody())
        assertTrue(dataSource.getRoles() is NetworkResult.Error)
    }

    // ── getRoleById ───────────────────────────────────────────────────────────

    @Test fun `getRoleById retourne le role en succes`() = runTest {
        coEvery { roleApi.rolesIdGet(2) } returns Response.success(fakeRoleDto)

        val result = dataSource.getRoleById(2)

        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.id)
    }

    @Test fun `getRoleById propage Error 404`() = runTest {
        coEvery { roleApi.rolesIdGet(any()) } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.getRoleById(1) is NetworkResult.Error)
    }

    // ── getAccountRole ───────────────────────────────────────────────────────

    @Test fun `getAccountRole retourne le role du compte`() = runTest {
        coEvery { accountApi.accountsRoleIdGet("acc-001") } returns Response.success(fakeRoleDto)

        val result = dataSource.getAccountRole("acc-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("CUSTOMER", (result as NetworkResult.Success).data.name)
    }

    // ── getPersonalInformations ───────────────────────────────────────────────

    @Test fun `getPersonalInformations retourne la liste`() = runTest {
        coEvery { personalInformationApi.personalInformationGet() } returns Response.success(listOf(fakePersonalInfoDto))

        val result = dataSource.getPersonalInformations()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Alice", result.data[0].firstname)
    }

    @Test fun `getPersonalInformations propage Error`() = runTest {
        coEvery { personalInformationApi.personalInformationGet() } returns Response.error(500, "error".toResponseBody())
        assertTrue(dataSource.getPersonalInformations() is NetworkResult.Error)
    }

    // ── getPersonalInformationById ────────────────────────────────────────────

    @Test fun `getPersonalInformationById retourne les info`() = runTest {
        coEvery { personalInformationApi.personalInformationIdGet(1) } returns Response.success(fakePersonalInfoDto)

        val result = dataSource.getPersonalInformationById(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals("alice@mail.fr", (result as NetworkResult.Success).data.email)
    }

    @Test fun `getPersonalInformationById propage Error 404`() = runTest {
        coEvery { personalInformationApi.personalInformationIdGet(any()) } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.getPersonalInformationById(1) is NetworkResult.Error)
    }

    // ── getPersonalInformationByAccountId ──────────────────────────────────────

    @Test fun `getPersonalInformationByAccountId retourne les info`() = runTest {
        coEvery { accountApi.accountsPersonalInformationIdGet("acc-001") } returns Response.success(fakePersonalInfoDto)

        val result = dataSource.getPersonalInformationByAccountId("acc-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", (result as NetworkResult.Success).data.firstname)
    }

    // ── createPersonalInformation ──────────────────────────────────────────────

    @Test fun `createPersonalInformation retourne les info creees`() = runTest {
        coEvery { personalInformationApi.personalInformationPost(any()) } returns Response.success(fakePersonalInfoDto)

        val request = PersonalInformationRegister(
            firstname = "Alice", lastname = "Dupont",
            email = "alice@mail.fr", address = "1 rue", phoneNumber = "06",
        )
        val result = dataSource.createPersonalInformation(request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alice", (result as NetworkResult.Success).data.firstname)
        coVerify(exactly = 1) { personalInformationApi.personalInformationPost(any()) }
    }

    @Test fun `createPersonalInformation propage Error 400`() = runTest {
        coEvery { personalInformationApi.personalInformationPost(any()) } returns Response.error(400, "invalid".toResponseBody())

        val request = PersonalInformationRegister(firstname = "A", lastname = "B", email = "a@b.fr", address = "", phoneNumber = "")
        assertTrue(dataSource.createPersonalInformation(request) is NetworkResult.Error)
    }

    // ── updatePersonalInformation ──────────────────────────────────────────────

    @Test fun `updatePersonalInformation retourne les info mises a jour`() = runTest {
        val updated = fakePersonalInfoDto.copy(firstname = "Alicia")
        coEvery { personalInformationApi.personalInformationIdPut(1, any()) } returns Response.success(updated)

        val pi = PersonalInformation(id = 1, firstname = "Alicia", lastname = "Dupont", email = "alice@mail.fr")
        val result = dataSource.updatePersonalInformation(1, pi)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Alicia", (result as NetworkResult.Success).data.firstname)
    }

    @Test fun `updatePersonalInformation propage Error`() = runTest {
        coEvery { personalInformationApi.personalInformationIdPut(any(), any()) } returns Response.error(404, "not found".toResponseBody())

        val pi = PersonalInformation(id = 1, firstname = "A", lastname = "B", email = "a@b.fr")
        assertTrue(dataSource.updatePersonalInformation(1, pi) is NetworkResult.Error)
    }
}

