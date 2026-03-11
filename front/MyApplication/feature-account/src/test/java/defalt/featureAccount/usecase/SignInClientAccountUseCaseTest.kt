package defalt.featureAccount.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.entity.account.SignInRequest
import defalt.domain.entity.account.TokenRequest
import defalt.domain.entity.account.TokenResponse
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
        // signIn renvoie le token
        coEvery { repository.signIn(any()) } returns NetworkResult.Success(TokenRequest(jwt = "fake-token"))
        // validateToken renvoie l'id et le rôle en string
        coEvery { repository.validateToken(any()) } returns NetworkResult.Success(TokenResponse(id = "acc-001", role = role))
    }

    @Test
    fun `retourne CUSTOMER quand le role est CUSTOMER`() = runTest {
        stubSignIn("CUSTOMER")

        val result = useCase("alice@mail.fr", "Password1!")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.CUSTOMER, (result as NetworkResult.Success).data)
    }

    @Test
    fun `retourne ADMIN quand le role est ADMIN`() = runTest {
        stubSignIn("ADMIN")

        val result = useCase("admin@bank.fr", "Admin1234!")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.ADMIN, (result as NetworkResult.Success).data)
    }

    @Test
    fun `retourne CUSTOMER quand le role est inconnu`() = runTest {
        // Si le rôle renvoyé n'est pas un enum valide, use case devrait retourner CUSTOMER par défaut
        stubSignIn("UNKNOWN")

        val result = useCase("x@y.fr", "pass")

        assertTrue(result is NetworkResult.Success)
        assertEquals(RoleEnum.CUSTOMER, (result as NetworkResult.Success).data)
    }

    @Test
    fun `propage l erreur si signIn echoue`() = runTest {
        coEvery { repository.signIn(any()) } returns NetworkResult.Error(401, "Unauthorized")

        val result = useCase("bad@mail.fr", "wrong")

        assertTrue(result is NetworkResult.Error)
        assertEquals(401, (result as NetworkResult.Error).code)
    }

    @Test
    fun `propage l exception reseau`() = runTest {
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
            NetworkResult.Success(TokenRequest(jwt = "t"))
        }
        coEvery { repository.validateToken(any()) } returns NetworkResult.Success(TokenResponse(id = "acc-001", role = "CUSTOMER"))

        useCase("alice@mail.fr", "MyPass!")

        assertEquals("alice@mail.fr", captured?.id)
        assertEquals("MyPass!", captured?.password)
    }
}
