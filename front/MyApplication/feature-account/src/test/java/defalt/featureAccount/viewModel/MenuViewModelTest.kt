package defalt.featureAccount.viewModel

import defalt.domain.session.Session
import defalt.featureAccount.usecase.LogoutUseCase
import defalt.testing.MainDispatcherRule
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class MenuViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test fun `logout appelle le useCase`() {
        val logoutUseCase: LogoutUseCase = mockk(relaxed = true)
        val viewModel = MenuViewModel(logoutUseCase)

        viewModel.logout()

        verify(exactly = 1) { logoutUseCase() }
    }

    @Test fun `logout vide la session via le vrai useCase`() {
        val session = Session(token = "tok", accountId = "acc-001", role = "CUSTOMER")
        val useCase = LogoutUseCase(session)
        val viewModel = MenuViewModel(useCase)

        viewModel.logout()

        assertNull(session.token)
        assertNull(session.accountId)
        assertNull(session.role)
    }
}
