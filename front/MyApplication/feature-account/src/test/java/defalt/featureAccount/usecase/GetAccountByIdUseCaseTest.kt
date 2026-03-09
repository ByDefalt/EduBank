package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
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

class GetAccountByIdUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: GetAccountByIdUseCase

    private val fakeAccount = Account(id = "acc-001", state = AccountStateEnum.ACTIVE)
    private val fakeInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")

    @Before
    fun setUp() {
        useCase = GetAccountByIdUseCase(repository, logger)
    }

    @Test
    fun `retourne AccountWithInfo quand les deux appels reussissent`() = runTest {
        coEvery { repository.getAccountById("acc-001") } returns NetworkResult.Success(fakeAccount)
        coEvery { repository.getPersonalInformationByAccountId("acc-001") } returns NetworkResult.Success(fakeInfo)

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Success)
        val data = (result as NetworkResult.Success).data
        assertEquals(fakeAccount, data.account)
        assertEquals(fakeInfo, data.personalInfo)
    }

    @Test
    fun `propage l erreur si getAccountById echoue`() = runTest {
        coEvery { repository.getAccountById("acc-001") } returns NetworkResult.Error(404, "Not found")

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Error)
        assertEquals("Not found", (result as NetworkResult.Error).message)
        coVerify(exactly = 0) { repository.getPersonalInformationByAccountId(any()) }
    }

    @Test
    fun `propage l erreur si getPersonalInformation echoue`() = runTest {
        coEvery { repository.getAccountById("acc-001") } returns NetworkResult.Success(fakeAccount)
        coEvery { repository.getPersonalInformationByAccountId("acc-001") } returns NetworkResult.Error(500, "Server error")

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Error)
        assertEquals("Server error", (result as NetworkResult.Error).message)
    }

    @Test
    fun `propage l exception si getAccountById lance une exception`() = runTest {
        val ex = RuntimeException("network failure")
        coEvery { repository.getAccountById("acc-001") } returns NetworkResult.Exception(ex)

        val result = useCase("acc-001")

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }
}
