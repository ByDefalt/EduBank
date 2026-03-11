package defalt.featureAccount.viewModel

import defalt.domain.entity.account.RoleEnum
import defalt.domain.repository.service.IAccountRepository
import defalt.domain.session.Session
import defalt.featureAccount.usecase.LogoutUseCase
import defalt.testing.MainDispatcherRule
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class MenuViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    @Test fun `logout appelle le useCase`() = runTest {
        val logoutUseCase: LogoutUseCase = mockk(relaxed = true)
        val viewModel = MenuViewModel(logoutUseCase)

        viewModel.logout()

        coVerify(exactly = 1) { logoutUseCase() }
    }

    @Test fun `logout vide la session via le vrai useCase`() = runTest {
        val session = Session(token = "tok", accountId = "acc-001", role = RoleEnum.CUSTOMER)
        val repository: IAccountRepository = mockk()
        coEvery { repository.signOut() } returns NetworkResult.Success(true)
        val useCase = LogoutUseCase(repository, session)
        val viewModel = MenuViewModel(useCase)

        viewModel.logout()

        assertNull(session.token)
        assertNull(session.accountId)
        assertNull(session.role)
    }
}
