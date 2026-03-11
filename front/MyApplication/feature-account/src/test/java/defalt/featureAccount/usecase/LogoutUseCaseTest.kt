package defalt.featureAccount.usecase

import defalt.domain.entity.account.RoleEnum
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var session: Session
    private lateinit var repository: IAccountRepository
    private lateinit var useCase: LogoutUseCase

    @Before fun setUp() {
        session = Session(token = "fake-token", accountId = "acc-001", role = RoleEnum.CUSTOMER)
        repository = mockk()
        coEvery { repository.signOut() } returns NetworkResult.Success(true)
        useCase = LogoutUseCase(repository, session)
    }

    @Test fun `efface le token de la session`() = runBlocking {
        useCase()
        assertNull(session.token)
        coVerify(exactly = 1) { repository.signOut() }
    }

    @Test fun `efface l accountId de la session`() = runBlocking {
        useCase()
        assertNull(session.accountId)
    }

    @Test fun `efface le role de la session`() = runBlocking {
        useCase()
        assertNull(session.role)
    }

    @Test fun `session est completement vide apres logout`() = runBlocking {
        useCase()
        assertNull(session.token)
        assertNull(session.accountId)
        assertNull(session.role)
    }

    @Test fun `peut etre appele plusieurs fois sans erreur`() = runBlocking {
        useCase()
        useCase()
        assertNull(session.token)
        coVerify(exactly = 2) { repository.signOut() }
    }

    @Test fun `ne touche pas une session deja vide`() = runBlocking {
        val emptySession = Session()
        val uc = LogoutUseCase(repository, emptySession)
        coEvery { repository.signOut() } returns NetworkResult.Success(true)
        uc()
        assertNull(emptySession.token)
    }
}
