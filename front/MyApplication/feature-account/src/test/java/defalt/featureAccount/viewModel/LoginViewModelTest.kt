package defalt.featureAccount.viewModel

import defalt.domain.entity.account.RoleEnum
import defalt.featureAccount.usecase.SignInClientAccountUseCase
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val signIn: SignInClientAccountUseCase = mockk()
    private val viewModel by lazy { LoginViewModel(signIn) }

    @Test fun `uiState initial est Idle`() {
        assertTrue(viewModel.uiState.value is UiState.Idle)
    }

    @Test fun `login passe en Success avec le role`() {
        coEvery { signIn("alice@mail.fr", "Password1!") } returns NetworkResult.Success(RoleEnum.CUSTOMER)

        viewModel.login("alice@mail.fr", "Password1!")

        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(RoleEnum.CUSTOMER, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test fun `login passe en Success ADMIN`() {
        coEvery { signIn("admin@bank.fr", "Admin1!") } returns NetworkResult.Success(RoleEnum.ADMIN)

        viewModel.login("admin@bank.fr", "Admin1!")

        assertEquals(RoleEnum.ADMIN, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test fun `login passe en Error si credentials incorrects`() {
        coEvery { signIn(any(), any()) } returns NetworkResult.Error(401, "Identifiants incorrects")

        viewModel.login("bad@mail.fr", "wrong")

        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Identifiants incorrects", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `login passe en Error si exception reseau`() {
        coEvery { signIn(any(), any()) } returns NetworkResult.Exception(RuntimeException("no internet"))

        viewModel.login("a@b.fr", "pass")

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test fun `login repasse en Loading puis Success`() {
        coEvery { signIn(any(), any()) } returns NetworkResult.Success(RoleEnum.CUSTOMER)

        viewModel.login("alice@mail.fr", "Password1!")


        assertTrue(viewModel.uiState.value is UiState.Success)
    }
}
