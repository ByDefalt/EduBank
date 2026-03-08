package defalt.featureOperation.viewModel

import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BeneficiariesViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getMyBeneficiaries: GetMyBeneficiaries = mockk()
    private lateinit var viewModel: BeneficiariesViewModel

    private val fakeBenefs = listOf(
        Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...1", name = "Alice"),
        Beneficiary(id = 2, accountSourceId = "acc-001", ibanTarget = "FR76...2", name = "Bob"),
    )

    @Before fun setUp() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Success(fakeBenefs)
        viewModel = BeneficiariesViewModel(getMyBeneficiaries)
    }

    @Test fun `init charge les beneficiaires en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, (viewModel.uiState.value as UiState.Success).data.size)
    }

    @Test fun `retry recharge les beneficiaires`() {
        viewModel.retry()
        coVerify(exactly = 2) { getMyBeneficiaries() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si useCase echoue`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Error(500, "Erreur")
        val vm = BeneficiariesViewModel(getMyBeneficiaries)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `passe en Error si exception`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = BeneficiariesViewModel(getMyBeneficiaries)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `query initial est vide`() {
        assertEquals("", viewModel.query.value)
    }

    @Test fun `onQueryChange met a jour la query`() {
        viewModel.onQueryChange("Alice")
        assertEquals("Alice", viewModel.query.value)
    }

    @Test fun `onQueryChange plusieurs fois garde la derniere valeur`() {
        viewModel.onQueryChange("A")
        viewModel.onQueryChange("Al")
        viewModel.onQueryChange("Alice")
        assertEquals("Alice", viewModel.query.value)
    }

    @Test fun `liste vide en Success`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Success(emptyList())
        val vm = BeneficiariesViewModel(getMyBeneficiaries)
        assertTrue((vm.uiState.value as UiState.Success).data.isEmpty())
    }
}

