package defalt.featureAccount.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignInClientAccountUseCaseTest {

    private val repository: IAccountRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var session: Session
    private lateinit var useCase: SignInClientAccountUseCase

    @Before
    fun setUp() {
        session = Session()
        useCase = SignInClientAccountUseCase(repository, session, logger)
    }

    private fun stubSignIn(role: String) {
        coEvery { repository.signIn(any()) } answers {
            session.token = "fake-token"
            session.role = role
            session.accountId = "acc-001"
            NetworkResult.Success(TokenRequest(jwt = "fake-token"))
        }
    }

    @Test
    fun `retourne CUSTOMER quand le rôle est CUSTOMER`() = runTest {
        stubSignIn("CUSTOMER")

        val result = useCase("alice@mail.fr", "Password1!")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.CUSTOMER, (result as NetworkResult.Success).data)
    }

    @Test
    fun `retourne ADMIN quand le rôle est ADMIN`() = runTest {
        stubSignIn("ADMIN")

        val result = useCase("admin@bank.fr", "Admin1234!")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.ADMIN, (result as NetworkResult.Success).data)
    }

    @Test
    fun `retourne CUSTOMER quand le rôle est inconnu`() = runTest {
        stubSignIn("UNKNOWN")

        val result = useCase("x@y.fr", "pass")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.CUSTOMER, (result as NetworkResult.Success).data)
    }

    @Test
    fun `propage l erreur si signIn échoue`() = runTest {
        coEvery { repository.signIn(any()) } returns NetworkResult.Error(401, "Unauthorized")

        val result = useCase("bad@mail.fr", "wrong")

        assertTrue(result is NetworkResult.Error)
        assertEquals(401, (result as NetworkResult.Error).code)
    }

    @Test
    fun `propage l exception réseau`() = runTest {
        val ex = RuntimeException("no internet")
        coEvery { repository.signIn(any()) } returns NetworkResult.Exception(ex)

        val result = useCase("a@b.fr", "pass")

        assertTrue(result is NetworkResult.Exception)
        assertEquals(ex, (result as NetworkResult.Exception).throwable)
    }

    @Test
    fun `envoie les bons identifiants au repository`() = runTest {
        var captured: SignInRequest? = null
        coEvery { repository.signIn(any()) } answers {
            captured = firstArg()
            session.role = "CUSTOMER"
            NetworkResult.Success(TokenRequest(jwt = "t"))
        }

        useCase("alice@mail.fr", "MyPass!")

        assertEquals("alice@mail.fr", captured?.id)
        assertEquals("MyPass!", captured?.password)
    }
}

