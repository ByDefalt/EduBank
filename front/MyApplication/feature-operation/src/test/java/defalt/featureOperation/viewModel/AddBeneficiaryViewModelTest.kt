package defalt.featureOperation.viewModel

import defalt.featureOperation.usecase.AddBeneficiary
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import defalt.domain.entity.operation.Beneficiary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AddBeneficiaryViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val addBeneficiary: AddBeneficiary = mockk()
    private val viewModel by lazy { AddBeneficiaryViewModel(addBeneficiary) }

    private val fakeBenef = Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...", name = "Alice")

    @Test fun `uiState initial est Idle`() {
        assertTrue(viewModel.uiState.value is UiState.Idle)
    }

    @Test fun `add passe en Success`() {
        coEvery { addBeneficiary("Alice", "FR76...") } returns NetworkResult.Success(fakeBenef)

        viewModel.add("Alice", "FR76...")

        assertTrue(viewModel.uiState.value is UiState.Success)
        coVerify(exactly = 1) { addBeneficiary("Alice", "FR76...") }
    }

    @Test fun `add passe en Error si useCase echoue`() {
        coEvery { addBeneficiary(any(), any()) } returns NetworkResult.Error(400, "IBAN invalide")

        viewModel.add("Bob", "INVALID")

        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("IBAN invalide", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `add passe en Error si exception`() {
        coEvery { addBeneficiary(any(), any()) } returns NetworkResult.Exception(RuntimeException("crash"))

        viewModel.add("Bob", "FR76...")

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test fun `add transmet le bon nom et iban`() {
        coEvery { addBeneficiary("Charlie", "FR76...3") } returns NetworkResult.Success(fakeBenef)

        viewModel.add("Charlie", "FR76...3")

        coVerify(exactly = 1) { addBeneficiary("Charlie", "FR76...3") }
    }
}

