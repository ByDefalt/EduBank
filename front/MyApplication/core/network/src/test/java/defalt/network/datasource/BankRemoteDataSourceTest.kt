package defalt.network.datasource.bank

import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.network.api.bank.model.BankAccount as BankAccountDto
import defalt.network.api.bank.model.BankAccountDetails as BankAccountDetailsDto
import defalt.network.api.bank.model.BankAccountParameter as BankAccountParameterDto
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
        id = 1, parameterId = 1, typeId = 1, sold = 1000.0, iban = "FR76...",
    )
    private val fakeDetailsDto = BankAccountDetailsDto(
        id = 1,
        parameterId = 1,
        typeId = 1,
        sold = 1000.0,
        iban = "FR76...",
        parameter = BankAccountParameterDto(id = 1, overdraftLimit = 500.0, state = BankAccountParameterDto.State.ACTIVE),
        type = TypeDto(id = 1, name = "CHEQUES"),
    )

    @Before fun setUp() {
        dataSource = BankRemoteDataSource(bankAccountApi, bankAccountParameterApi)
    }

    // ── adminGetAllBankAccounts ─────────────────────────────────────────────

    @Test fun `adminGetAllBankAccounts retourne la liste en succes`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet() } returns
            Response.success(listOf(fakeDetailsDto))

        val result = dataSource.adminGetAllBankAccounts()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("1", result.data[0].id)
    }

    @Test fun `adminGetAllBankAccounts propage Error`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet() } returns
            Response.error(500, "error".toResponseBody())

        assertTrue(dataSource.adminGetAllBankAccounts() is NetworkResult.Error)
    }

    @Test fun `adminGetAllBankAccounts propage Exception`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet() } throws RuntimeException("crash")
        assertTrue(dataSource.adminGetAllBankAccounts() is NetworkResult.Exception)
    }

    // ── adminGetBankAccountById ─────────────────────────────────────────────

    @Test fun `adminGetBankAccountById retourne le detail en succes`() = runTest {
        coEvery { bankAccountApi.bankAccountsIdGet(1) } returns
            Response.success(fakeDetailsDto)

        val result = dataSource.adminGetBankAccountById("1")

        assertTrue(result is NetworkResult.Success)
        assertEquals("1", (result as NetworkResult.Success).data.id)
    }

    @Test fun `adminGetBankAccountById propage Error 404`() = runTest {
        coEvery { bankAccountApi.bankAccountsIdGet(1) } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(dataSource.adminGetBankAccountById("1") is NetworkResult.Error)
    }

    // ── adminDeleteBankAccount ──────────────────────────────────────────────

    @Test fun `adminDeleteBankAccount retourne Success Unit`() = runTest {
        coEvery { bankAccountApi.bankAccountsIdDelete(1) } returns
            Response.success(Unit)

        val result = dataSource.adminDeleteBankAccount("1")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { bankAccountApi.bankAccountsIdDelete(1) }
    }

    @Test fun `adminDeleteBankAccount propage Error 404`() = runTest {
        coEvery { bankAccountApi.bankAccountsIdDelete(any()) } returns
            Response.error(404, "not found".toResponseBody())

        assertTrue(dataSource.adminDeleteBankAccount("1") is NetworkResult.Error)
    }

    // ── adminCreateBankAccount ──────────────────────────────────────────────

    @Test fun `adminCreateBankAccount retourne le detail en succes`() = runTest {
        coEvery { bankAccountApi.bankAccountsPost(any()) } returns
            Response.success(fakeBankAccountDto)
        coEvery { bankAccountApi.bankAccountsIdGet(1) } returns
            Response.success(fakeDetailsDto)

        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        val result = dataSource.adminCreateBankAccount(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("1", (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { bankAccountApi.bankAccountsPost(any()) }
        coVerify(exactly = 1) { bankAccountApi.bankAccountsIdGet(1) }
    }

    @Test fun `adminCreateBankAccount propage Error 409`() = runTest {
        coEvery { bankAccountApi.bankAccountsPost(any()) } returns
            Response.error(409, "iban exists".toResponseBody())

        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 0.0, state = State.ACTIVE)
        val result = dataSource.adminCreateBankAccount(1, request)

        assertTrue(result is NetworkResult.Error)
        assertEquals(409, (result as NetworkResult.Error).code)
        // Ne doit pas appeler bankAccountsIdGet si le POST échoue
        coVerify(exactly = 0) { bankAccountApi.bankAccountsIdGet(any()) }
    }

    // ── adminUpdateBankAccountParameters ────────────────────────────────────

    @Test fun `adminUpdateBankAccountParameters retourne Success`() = runTest {
        coEvery { bankAccountParameterApi.parametersIdPut(1, any()) } returns
            Response.success(BankAccountParameterDto(id = 1, overdraftLimit = 200.0, state = BankAccountParameterDto.State.ACTIVE))

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccountParameters("1", param)

        assertTrue(result is NetworkResult.Success)
    }

    // ── adminUpdateBankAccount ───────────────────────────────────────────────

    @Test fun `adminUpdateBankAccount met a jour param puis recharge le detail`() = runTest {
        coEvery { bankAccountParameterApi.parametersIdPut(1, any()) } returns
            Response.success(BankAccountParameterDto(id = 1, overdraftLimit = 200.0, state = BankAccountParameterDto.State.ACTIVE))
        coEvery { bankAccountApi.bankAccountsIdGet(1) } returns
            Response.success(fakeDetailsDto)

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccount("1", 1, param)

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { bankAccountParameterApi.parametersIdPut(1, any()) }
        coVerify(exactly = 1) { bankAccountApi.bankAccountsIdGet(1) }
    }

    @Test fun `adminUpdateBankAccount retourne Error si parametre echoue`() = runTest {
        coEvery { bankAccountParameterApi.parametersIdPut(any(), any()) } returns
            Response.error(400, "invalid".toResponseBody())

        val param = BankAccountParameter(id = 1, overdraftLimit = 200.0, state = State.ACTIVE)
        val result = dataSource.adminUpdateBankAccount("1", 1, param)

        assertTrue(result is NetworkResult.Error)
        // Ne doit pas recharger le detail si les params echouent
        coVerify(exactly = 0) { bankAccountApi.bankAccountsIdGet(any()) }
    }

    // ── getMyBankAccounts ───────────────────────────────────────────────────

    @Test fun `getMyBankAccounts retourne la liste en succes`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet(typeId = null) } returns
            Response.success(listOf(fakeDetailsDto))

        val result = dataSource.getMyBankAccounts(null)

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
    }

    @Test fun `getMyBankAccounts filtre par typeId`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet(typeId = 1) } returns
            Response.success(listOf(fakeDetailsDto))

        dataSource.getMyBankAccounts(1)

        coVerify(exactly = 1) { bankAccountApi.bankAccountsGet(typeId = 1) }
    }

    @Test fun `getMyBankAccounts propage Error`() = runTest {
        coEvery { bankAccountApi.bankAccountsGet(typeId = any()) } returns
            Response.error(401, "unauthorized".toResponseBody())

        assertTrue(dataSource.getMyBankAccounts(null) is NetworkResult.Error)
    }

    // ── getMyBankAccountById ────────────────────────────────────────────────

    @Test fun `getMyBankAccountById retourne le detail`() = runTest {
        coEvery { bankAccountApi.bankAccountsIdGet(1) } returns
            Response.success(fakeDetailsDto)

        val result = dataSource.getMyBankAccountById("1")

        assertTrue(result is NetworkResult.Success)
        assertEquals("1", (result as NetworkResult.Success).data.id)
    }
}
