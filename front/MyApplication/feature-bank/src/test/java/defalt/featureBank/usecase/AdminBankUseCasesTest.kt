package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountCreateRequest
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.State
import defalt.domain.repository.service.IBankRepository
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// ── AdminGetAllBankAccountsUseCase ───────────────────────────────────────────

class AdminGetAllBankAccountsUseCaseTest {

    private val repository: IBankRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: AdminGetAllBankAccountsUseCase

    @Before fun setUp() { useCase = AdminGetAllBankAccountsUseCase(repository, logger) }

    @Test fun `retourne la liste des comptes bancaires`() = runTest {
        val accounts = listOf(
            BankAccount(id = "bank-001", parameterId = 1, typeId = 1, sold = 500.0, iban = "FR76...1"),
            BankAccount(id = "bank-002", parameterId = 2, typeId = 2, sold = 1500.0, iban = "FR76...2"),
        )
        coEvery { repository.adminGetAllBankAccounts() } returns NetworkResult.Success(accounts)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { repository.adminGetAllBankAccounts() }
    }

    @Test fun `retourne liste vide`() = runTest {
        coEvery { repository.adminGetAllBankAccounts() } returns NetworkResult.Success(emptyList())
        assertTrue((useCase() as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.adminGetAllBankAccounts() } returns NetworkResult.Error(500, "Erreur")
        assertTrue(useCase() is NetworkResult.Error)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.adminGetAllBankAccounts() } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase() is NetworkResult.Exception)
    }
}

// ── AdminGetBankAccountByIdUseCase ───────────────────────────────────────────

class AdminGetBankAccountByIdUseCaseTest {

    private val repository: IBankRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: AdminGetBankAccountByIdUseCase

    private val fakeDetail = BankAccountDetail(id = "bank-001", sold = 1000.0, iban = "FR76...")

    @Before fun setUp() { useCase = AdminGetBankAccountByIdUseCase(repository, logger) }

    @Test fun `retourne le detail du compte en succes`() = runTest {
        coEvery { repository.adminGetBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetail)

        val result = useCase("bank-001")

        assertTrue(result is NetworkResult.Success)
        assertEquals(fakeDetail, (result as NetworkResult.Success).data)
        coVerify(exactly = 1) { repository.adminGetBankAccountById("bank-001") }
    }

    @Test fun `propage l erreur 404`() = runTest {
        coEvery { repository.adminGetBankAccountById("bank-001") } returns NetworkResult.Error(404, "Non trouve")
        val result = useCase("bank-001")
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.adminGetBankAccountById(any()) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase("bank-001") is NetworkResult.Exception)
    }
}

// ── AdminDeleteBankAccountUseCase ────────────────────────────────────────────

class AdminDeleteBankAccountUseCaseTest {

    private val repository: IBankRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: AdminDeleteBankAccountUseCase

    @Before fun setUp() { useCase = AdminDeleteBankAccountUseCase(repository, logger) }

    @Test fun `supprime le compte en succes`() = runTest {
        coEvery { repository.adminDeleteBankAccount("bank-001") } returns NetworkResult.Success(Unit)

        val result = useCase("bank-001")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { repository.adminDeleteBankAccount("bank-001") }
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.adminDeleteBankAccount("bank-001") } returns NetworkResult.Error(404, "Non trouve")
        assertTrue(useCase("bank-001") is NetworkResult.Error)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.adminDeleteBankAccount(any()) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(useCase("bank-001") is NetworkResult.Exception)
    }
}

// ── AdminCreateBankAccountUseCase ────────────────────────────────────────────

class AdminCreateBankAccountUseCaseTest {

    private val repository: IBankRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: AdminCreateBankAccountUseCase

    private val fakeDetail = BankAccountDetail(id = "bank-001", sold = 0.0, iban = "FR76...")

    @Before fun setUp() { useCase = AdminCreateBankAccountUseCase(repository, logger) }

    @Test fun `cree le compte bancaire en succes`() = runTest {
        val slot = slot<BankAccountCreateRequest>()
        coEvery { repository.adminCreateBankAccount(1, capture(slot)) } returns NetworkResult.Success(fakeDetail)

        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        val result = useCase(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("FR76...", slot.captured.iban)
        assertEquals(1, slot.captured.typeId)
        coVerify(exactly = 1) { repository.adminCreateBankAccount(1, any()) }
    }

    @Test fun `propage l erreur IBAN deja utilise`() = runTest {
        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        coEvery { repository.adminCreateBankAccount(any(), any()) } returns NetworkResult.Error(409, "IBAN deja utilise")

        val result = useCase(1, request)

        assertTrue(result is NetworkResult.Error)
        assertEquals(409, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        val request = BankAccountCreateRequest(typeId = 1, iban = "FR76...", sold = 0.0, overdraftLimit = 500.0, state = State.ACTIVE)
        coEvery { repository.adminCreateBankAccount(any(), any()) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase(1, request) is NetworkResult.Exception)
    }
}
