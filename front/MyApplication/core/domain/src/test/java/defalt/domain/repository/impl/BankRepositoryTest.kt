package defalt.domain.repository.impl

import defalt.domain.datasource.bank.IBankRemoteDataSource
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BankRepositoryTest {

    private val dataSource: IBankRemoteDataSource = mockk()
    private lateinit var repository: BankRepository

    private val fakeAccount = BankAccount(id = "bank-001", sold = 1000.0, iban = "FR76...", typeId = 1)
    private val fakeDetail = BankAccountDetail(
        id = "bank-001",
        sold = 1000.0,
        iban = "FR76...",
        type = Type(1, "CHEQUES"),
        parameter = BankAccountParameter(1, 500.0, State.ACTIVE),
    )

    @Before fun setUp() { repository = BankRepository(dataSource) }

    // ── admin ─────────────────────────────────────────────────────────────────

    @Test fun `adminGetAllBankAccounts delegue`() = runTest {
        coEvery { dataSource.adminGetAllBankAccounts() } returns NetworkResult.Success(listOf(fakeAccount))
        val result = repository.adminGetAllBankAccounts()
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { dataSource.adminGetAllBankAccounts() }
    }

    @Test fun `adminGetAllBankAccounts propage Error`() = runTest {
        coEvery { dataSource.adminGetAllBankAccounts() } returns NetworkResult.Error(500, "err")
        assertTrue(repository.adminGetAllBankAccounts() is NetworkResult.Error)
    }

    @Test fun `adminGetBankAccountById delegue l id`() = runTest {
        coEvery { dataSource.adminGetBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetail)
        val result = repository.adminGetBankAccountById("bank-001")
        assertTrue(result is NetworkResult.Success)
        assertEquals("bank-001", (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { dataSource.adminGetBankAccountById("bank-001") }
    }

    @Test fun `adminGetBankAccountById propage Error 404`() = runTest {
        coEvery { dataSource.adminGetBankAccountById(any()) } returns NetworkResult.Error(404, "not found")
        assertTrue(repository.adminGetBankAccountById("bank-001") is NetworkResult.Error)
    }

    @Test fun `adminGetBankAccountsByAccountId delegue accountId`() = runTest {
        coEvery { dataSource.adminGetBankAccountsByAccountId("1") } returns NetworkResult.Success(listOf(fakeAccount))
        val result = repository.adminGetBankAccountsByAccountId("1")
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.adminGetBankAccountsByAccountId("1") }
    }

    @Test fun `adminCreateBankAccount delegue`() = runTest {
        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        coEvery { dataSource.adminCreateBankAccount("1", request) } returns NetworkResult.Success(fakeDetail)
        val result = repository.adminCreateBankAccount("1", request)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.adminCreateBankAccount("1", request) }
    }

    @Test fun `adminDeleteBankAccount delegue`() = runTest {
        coEvery { dataSource.adminDeleteBankAccount("bank-001") } returns NetworkResult.Success(Unit)
        val result = repository.adminDeleteBankAccount("bank-001")
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.adminDeleteBankAccount("bank-001") }
    }

    @Test fun `adminUpdateBankAccountParameters delegue`() = runTest {
        val param = BankAccountParameter(1, 200.0, State.ACTIVE)
        coEvery { dataSource.adminUpdateBankAccountParameters("bank-001", param) } returns NetworkResult.Success(Unit)
        assertTrue(repository.adminUpdateBankAccountParameters("bank-001", param) is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.adminUpdateBankAccountParameters("bank-001", param) }
    }

    @Test fun `adminUpdateBankAccount delegue`() = runTest {
        val param = BankAccountParameter(1, 200.0, State.ACTIVE)
        coEvery { dataSource.adminUpdateBankAccountParameters("bank-001", param) } returns NetworkResult.Success(
            Unit)
        assertTrue(repository.adminUpdateBankAccountParameters("bank-001", param) is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.adminUpdateBankAccountParameters("bank-001", param) }
    }

    // ── client ────────────────────────────────────────────────────────────────

    @Test fun `getMyBankAccounts delegue typeId null`() = runTest {
        coEvery { dataSource.getMyBankAccounts(null) } returns NetworkResult.Success(listOf(fakeAccount))
        val result = repository.getMyBankAccounts(null)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.getMyBankAccounts(null) }
    }

    @Test fun `getMyBankAccounts delegue typeId non null`() = runTest {
        coEvery { dataSource.getMyBankAccounts(1) } returns NetworkResult.Success(listOf(fakeAccount))
        val result = repository.getMyBankAccounts(1)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.getMyBankAccounts(1) }
    }

    @Test fun `getMyBankAccountById delegue`() = runTest {
        coEvery { dataSource.getMyBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetail)
        val result = repository.getMyBankAccountById("bank-001")
        assertTrue(result is NetworkResult.Success)
        assertEquals("bank-001", (result as NetworkResult.Success).data.id)
    }

    @Test fun `getMyBankAccountCoHolders delegue`() = runTest {
        coEvery { dataSource.getMyBankAccountCoHolders("bank-001") } returns NetworkResult.Success(listOf("1", "2"))
        val result = repository.getMyBankAccountCoHolders("bank-001")
        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
    }

    @Test fun `getMyBankAccounts propage Exception`() = runTest {
        coEvery { dataSource.getMyBankAccounts(any()) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(repository.getMyBankAccounts(null) is NetworkResult.Exception)
    }
}
