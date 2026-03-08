package defalt.network.datasource.bank

import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.network.api.bank.model.BankAccount as BankAccountDto
import defalt.network.api.bank.model.BankAccountDetail as BankAccountDetailDto
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto
import defalt.network.api.bank.model.State as StateDto
import defalt.network.api.bank.model.Type as TypeDto
import defalt.network.api.bank.service.BankAccountApi
import defalt.network.api.bank.service.BankAccountParameterApi
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BankRemoteDataSourceTest {

    private val bankAccountApi: BankAccountApi = mockk()
    private val bankAccountParameterApi: BankAccountParameterApi = mockk()
    private lateinit var dataSource: BankRemoteDataSource

    private val fakeBankAccountDto = BankAccountDto(
        id = "bank-001", parameterId = 1, typeId = 1, sold = 1000.0, iban = "FR76...",
    )
    private val fakeDetailDto = BankAccountDetailDto(
        id = "bank-001",
        parameter = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = StateDto.ACTIVE),
        type = TypeDto(id = 1, name = "CHEQUES"),
        sold = 1000.0, iban = "FR76...",
    )

    @Before fun setUp() {
        dataSource = BankRemoteDataSource(bankAccountApi, bankAccountParameterApi)
    }

    // ── adminGetAllBankAccounts ─────────────────────────────────────────────

    @Test fun `adminGetAllBankAccounts retourne la liste en succes`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsGet() } returns
            Response.success(listOf(fakeBankAccountDto))

        val result = dataSource.adminGetAllBankAccounts()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("bank-001", result.data[0].id)
    }

    @Test fun `adminGetAllBankAccounts propage Error`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsGet() } returns
            Response.error(500, "error".toResponseBody())

        assertTrue(dataSource.adminGetAllBankAccounts() is NetworkResult.Error)
    }

    @Test fun `adminGetAllBankAccounts propage Exception`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsGet() } throws RuntimeException("crash")
        assertTrue(dataSource.adminGetAllBankAccounts() is NetworkResult.Exception)
    }

    // ── adminGetBankAccountById ─────────────────────────────────────────────

    @Test fun `adminGetBankAccountById retourne le detail en succes`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsIdGet("bank-001") } returns
            Response.success(fakeDetailDto)

        val result = dataSource.adminGetBankAccountById("bank-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("bank-001", (result as NetworkResult.Success).data.id)
    }

    @Test fun `adminGetBankAccountById propage Error 404`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsIdGet("bank-001") } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(dataSource.adminGetBankAccountById("bank-001") is NetworkResult.Error)
    }

    // ── adminDeleteBankAccount ──────────────────────────────────────────────

    @Test fun `adminDeleteBankAccount retourne Success Unit`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsIdDelete("bank-001") } returns
            Response.success(Unit)

        val result = dataSource.adminDeleteBankAccount("bank-001")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { bankAccountApi.adminBankAccountsIdDelete("bank-001") }
    }

    @Test fun `adminDeleteBankAccount propage Error 404`() = runTest {
        coEvery { bankAccountApi.adminBankAccountsIdDelete(any()) } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(dataSource.adminDeleteBankAccount("bank-001") is NetworkResult.Error)
    }

    // ── adminCreateBankAccount ──────────────────────────────────────────────

    @Test fun `adminCreateBankAccount retourne le detail en succes`() = runTest {
        coEvery { bankAccountApi.adminAccountsAccountIdBankAccountsPost(1, any()) } returns
            Response.success(fakeDetailDto)

        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        val result = dataSource.adminCreateBankAccount(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("bank-001", (result as NetworkResult.Success).data.id)
    }

    @Test fun `adminCreateBankAccount propage Error 409`() = runTest {
        coEvery { bankAccountApi.adminAccountsAccountIdBankAccountsPost(any(), any()) } returns
            Response.error(409, "iban exists".toResponseBody())

        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 0.0, state = State.ACTIVE)
        val result = dataSource.adminCreateBankAccount(1, request)

        assertTrue(result is NetworkResult.Error)
        assertEquals(409, (result as NetworkResult.Error).code)
    }

    // ── adminUpdateBankAccountParameters ────────────────────────────────────

    @Test fun `adminUpdateBankAccountParameters retourne Success`() = runTest {
        coEvery { bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch("bank-001", any()) } returns
            Response.success(Unit)

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccountParameters("bank-001", param)

        assertTrue(result is NetworkResult.Success)
    }

    // ── adminUpdateBankAccount ───────────────────────────────────────────────

    @Test fun `adminUpdateBankAccount met a jour param puis recharge le detail`() = runTest {
        coEvery { bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch("bank-001", any()) } returns
            Response.success(Unit)
        coEvery { bankAccountApi.adminBankAccountsIdGet("bank-001") } returns
            Response.success(fakeDetailDto)

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccount("bank-001", 1, param)

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch("bank-001", any()) }
        coVerify(exactly = 1) { bankAccountApi.adminBankAccountsIdGet("bank-001") }
    }

    @Test fun `adminUpdateBankAccount retourne Error si parametre echoue`() = runTest {
        coEvery { bankAccountParameterApi.adminBankAccountsBankAccountIdParametersPatch(any(), any()) } returns
            Response.error(400, "invalid".toResponseBody())

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccount("bank-001", 1, param)

        assertTrue(result is NetworkResult.Error)
        // Ne doit pas recharger le detail si les params echouent
        coVerify(exactly = 0) { bankAccountApi.adminBankAccountsIdGet(any()) }
    }

    // ── getMyBankAccounts ───────────────────────────────────────────────────

    @Test fun `getMyBankAccounts retourne la liste en succes`() = runTest {
        coEvery { bankAccountApi.myBankAccountsGet(null) } returns
            Response.success(listOf(fakeBankAccountDto))

        val result = dataSource.getMyBankAccounts(null)

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
    }

    @Test fun `getMyBankAccounts filtre par typeId`() = runTest {
        coEvery { bankAccountApi.myBankAccountsGet(1) } returns
            Response.success(listOf(fakeBankAccountDto))

        dataSource.getMyBankAccounts(1)

        coVerify(exactly = 1) { bankAccountApi.myBankAccountsGet(1) }
    }

    @Test fun `getMyBankAccounts propage Error`() = runTest {
        coEvery { bankAccountApi.myBankAccountsGet(any()) } returns
            Response.error(401, "unauthorized".toResponseBody())

        assertTrue(dataSource.getMyBankAccounts(null) is NetworkResult.Error)
    }

    // ── getMyBankAccountById ────────────────────────────────────────────────

    @Test fun `getMyBankAccountById retourne le detail`() = runTest {
        coEvery { bankAccountApi.myBankAccountsIdGet("bank-001") } returns
            Response.success(fakeDetailDto)

        val result = dataSource.getMyBankAccountById("bank-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals("bank-001", (result as NetworkResult.Success).data.id)
    }
}

