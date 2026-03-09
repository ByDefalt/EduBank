package defalt.featureBank.usecase

import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.repository.service.IBankRepository
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAllMyAccountTest {

    private val bankRepository: IBankRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: GetAllMyAccount

    private val fakeAccounts = listOf(
        BankAccount(id = "bank-001", parameterId = 1, typeId = 1, sold = 500.0, iban = "FR76...1"),
        BankAccount(id = "bank-002", parameterId = 2, typeId = 2, sold = 1500.0, iban = "FR76...2"),
    )
    private val fakeDetails = mapOf(
        "bank-001" to BankAccountDetail(id = "bank-001", sold = 500.0, iban = "FR76...1"),
        "bank-002" to BankAccountDetail(id = "bank-002", sold = 1500.0, iban = "FR76...2"),
    )

    @Before fun setUp() { useCase = GetAllMyAccount(bankRepository, logger) }

    @Test fun `retourne les details de tous les comptes`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(fakeAccounts)
        coEvery { bankRepository.getMyBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetails["bank-001"]!!)
        coEvery { bankRepository.getMyBankAccountById("bank-002") } returns NetworkResult.Success(fakeDetails["bank-002"]!!)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(2, data.size)
        assertEquals("bank-001", data[0].id)
        assertEquals("bank-002", data[1].id)
    }

    @Test fun `ignore les comptes dont le detail echoue`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(fakeAccounts)
        coEvery { bankRepository.getMyBankAccountById("bank-001") } returns NetworkResult.Error(404, "Non trouve")
        coEvery { bankRepository.getMyBankAccountById("bank-002") } returns NetworkResult.Success(fakeDetails["bank-002"]!!)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(1, data.size)
        assertEquals("bank-002", data[0].id)
    }

    @Test fun `retourne liste vide si aucun compte`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Success(emptyList())

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data.isEmpty())
        coVerify(exactly = 0) { bankRepository.getMyBankAccountById(any()) }
    }

    @Test fun `propage l erreur si getMyBankAccounts echoue`() = runTest {
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Error(500, "Erreur")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals("Erreur", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception reseau`() = runTest {
        val ex = RuntimeException("crash")
        coEvery { bankRepository.getMyBankAccounts() } returns NetworkResult.Exception(ex)

        val result = useCase()

        assertTrue(result is NetworkResult.Exception)
    }
}
