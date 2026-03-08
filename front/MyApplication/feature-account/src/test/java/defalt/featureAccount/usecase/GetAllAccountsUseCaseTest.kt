package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.repository.service.IAccountRepository
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

class GetAllAccountsUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: GetAllAccountsUseCase

    @Before
    fun setUp() {
        useCase = GetAllAccountsUseCase(repository, logger)
    }

    @Test
    fun `retourne la liste des comptes en succes`() = runTest {
        val accounts = listOf(
            Account(id = "acc-001", state = AccountStateEnum.ACTIVE),
            Account(id = "acc-002", state = AccountStateEnum.INACTIVE),
        )
        coEvery { repository.getAccounts() } returns NetworkResult.Success(accounts)

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { repository.getAccounts() }
    }

    @Test
    fun `retourne une liste vide en succes`() = runTest {
        coEvery { repository.getAccounts() } returns NetworkResult.Success(emptyList())

        val result = useCase()

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test
    fun `propage l erreur reseau`() = runTest {
        coEvery { repository.getAccounts() } returns NetworkResult.Error(503, "Service unavailable")

        val result = useCase()

        assertTrue(result is NetworkResult.Error)
        assertEquals(503, (result as NetworkResult.Error).code)
    }

    @Test
    fun `propage l exception`() = runTest {
        val ex = RuntimeException("timeout")
        coEvery { repository.getAccounts() } returns NetworkResult.Exception(ex)

        val result = useCase()

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }
}
