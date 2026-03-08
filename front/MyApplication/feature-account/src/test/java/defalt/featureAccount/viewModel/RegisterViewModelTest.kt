package defalt.featureAccount.viewModel

import defalt.domain.entity.account.Account
import defalt.featureAccount.usecase.RegisterClientAccountUseCase
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val registerUseCase: RegisterClientAccountUseCase = mockk()
    private val viewModel by lazy { RegisterViewModel(registerUseCase) }

    private fun callRegister() = viewModel.register(
        email = "alice@mail.fr",
        password = "Password1!",
        firstname = "Alice",
        lastname = "Dupont",
        address = "1 rue de Paris",
        phoneNumber = "0600000000",
    )

    @Test fun `uiState initial est Idle`() {
        assertTrue(viewModel.uiState.value is UiState.Idle)
    }

    @Test fun `register passe en Success`() {
        coEvery { registerUseCase(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(Account(id = "acc-001"))

        callRegister()

        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `register passe en Error si le useCase echoue`() {
        coEvery { registerUseCase(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Error(400, "Email deja utilise")

        callRegister()

        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Email deja utilise", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `register passe en Error si exception reseau`() {
        coEvery { registerUseCase(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Exception(RuntimeException("no internet"))

        callRegister()

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test fun `register appelle useCase avec les bons parametres`() {
        coEvery { registerUseCase(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(Account(id = "acc-001"))

        callRegister()

        coVerify(exactly = 1) {
            registerUseCase(
                "alice@mail.fr",
                "Password1!",
                "Alice",
                "Dupont",
                "1 rue de Paris",
                "0600000000",
            )
        }
    }
}

