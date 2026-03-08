package defalt.featureAccount.usecase

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountRegister
import defalt.domain.repository.service.IAccountRepository
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

class RegisterClientAccountUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: RegisterClientAccountUseCase

    @Before fun setUp() { useCase = RegisterClientAccountUseCase(repository, logger) }

    @Test fun `cree le compte avec les bonnes donnees`() = runTest {
        val slot = slot<AccountRegister>()
        val fakeAccount = Account(id = "acc-001")
        coEvery { repository.createAccount(capture(slot)) } returns NetworkResult.Success(fakeAccount)

        val result = useCase(
            email = "alice@mail.fr",
            password = "Password1!",
            firstname = "Alice",
            lastname = "Dupont",
            address = "1 rue de Paris",
            phoneNumber = "0600000000",
        )

        assertTrue(result is NetworkResult.Success)
        assertEquals("alice@mail.fr", slot.captured.personalInfo.email)
        assertEquals("Alice", slot.captured.personalInfo.firstname)
        assertEquals("Dupont", slot.captured.personalInfo.lastname)
        assertEquals("1 rue de Paris", slot.captured.personalInfo.address)
        assertEquals("0600000000", slot.captured.personalInfo.phoneNumber)
        assertEquals("Password1!", slot.captured.password)
    }

    @Test fun `propage l erreur du repository`() = runTest {
        coEvery { repository.createAccount(any()) } returns NetworkResult.Error(400, "Email deja utilise")

        val result = useCase("a@b.fr", "pass", "fn", "ln", "addr", "06")

        assertTrue(result is NetworkResult.Error)
        assertEquals("Email deja utilise", (result as NetworkResult.Error).message)
        coVerify(exactly = 1) { repository.createAccount(any()) }
    }

    @Test fun `propage l exception reseau`() = runTest {
        val ex = RuntimeException("no internet")
        coEvery { repository.createAccount(any()) } returns NetworkResult.Exception(ex)

        val result = useCase("a@b.fr", "pass", "fn", "ln", "addr", "06")

        assertTrue(result is NetworkResult.Exception)
    }
}
