package defalt.featureAccount.usecase

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

class ActivateAccountUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: ActivateAccountUseCase

    @Before fun setUp() { useCase = ActivateAccountUseCase(repository, logger) }

    @Test fun `retourne true en succes`() = runTest {
        coEvery { repository.activateAccount("acc-001") } returns NetworkResult.Success(true)

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data)
        coVerify(exactly = 1) { repository.activateAccount("acc-001") }
    }

    @Test fun `propage l erreur 400`() = runTest {
        coEvery { repository.activateAccount("acc-001") } returns NetworkResult.Error(400, "Deja actif")

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Error)
        assertEquals(400, (result as NetworkResult.Error).code)
        assertEquals("Deja actif", result.message)
    }

    @Test fun `propage l exception`() = runTest {
        val ex = RuntimeException("crash")
        coEvery { repository.activateAccount("acc-001") } returns NetworkResult.Exception(ex)

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }
}

class DeactivateAccountUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: DeactivateAccountUseCase

    @Before fun setUp() { useCase = DeactivateAccountUseCase(repository, logger) }

    @Test fun `retourne true en succes`() = runTest {
        coEvery { repository.deactivateAccount("acc-001") } returns NetworkResult.Success(true)

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { repository.deactivateAccount("acc-001") }
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.deactivateAccount("acc-001") } returns NetworkResult.Error(400, "Deja inactif")

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Error)
        assertEquals("Deja inactif", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.deactivateAccount("acc-001") } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase("acc-001") is NetworkResult.Exception)
    }
}

