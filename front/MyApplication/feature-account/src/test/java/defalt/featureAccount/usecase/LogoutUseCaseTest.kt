package defalt.featureAccount.usecase

import defalt.domain.session.Session
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var session: Session
    private lateinit var useCase: LogoutUseCase

    @Before fun setUp() {
        session = Session(token = "fake-token", accountId = "acc-001", role = "CUSTOMER")
        useCase = LogoutUseCase(session)
    }

    @Test fun `efface le token de la session`() {
        useCase()
        assertNull(session.token)
    }

    @Test fun `efface l accountId de la session`() {
        useCase()
        assertNull(session.accountId)
    }

    @Test fun `efface le role de la session`() {
        useCase()
        assertNull(session.role)
    }

    @Test fun `session est completement vide apres logout`() {
        useCase()
        assertNull(session.token)
        assertNull(session.accountId)
        assertNull(session.role)
    }

    @Test fun `peut etre appele plusieurs fois sans erreur`() {
        useCase()
        useCase()
        assertNull(session.token)
    }

    @Test fun `ne touche pas une session deja vide`() {
        val emptySession = Session()
        val uc = LogoutUseCase(emptySession)
        uc()
        assertNull(emptySession.token)
    }
}

