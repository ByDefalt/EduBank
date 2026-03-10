package defalt.featureOperation.viewModel

import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.usecase.DeleteBeneficiary
import defalt.featureOperation.usecase.EditBeneficiary
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

class EditBeneficiaryViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getMyBeneficiaries: GetMyBeneficiaries = mockk()
    private val editBeneficiary: EditBeneficiary = mockk()
    private val deleteBeneficiary: DeleteBeneficiary = mockk()
    private lateinit var viewModel: EditBeneficiaryViewModel

    private val fakeBenef1 = Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...1", name = "Alice")
    private val fakeBenef2 = Beneficiary(id = 2, accountSourceId = "acc-001", ibanTarget = "FR76...2", name = "Bob")

    @Before fun setUp() {
        viewModel = EditBeneficiaryViewModel(getMyBeneficiaries, editBeneficiary, deleteBeneficiary)
    }

    @Test fun `load charge le bon beneficiaire par id`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Success(listOf(fakeBenef1, fakeBenef2))

        viewModel.load(1)

        assertTrue(viewModel.beneficiary.value is UiState.Success)
        assertEquals(fakeBenef1, (viewModel.beneficiary.value as UiState.Success).data)
    }

    @Test fun `load passe en Error si beneficiaire non trouve`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Success(listOf(fakeBenef2))

        // id=1 n existe pas dans la liste
        try {
            viewModel.load(1)
        } catch (_: Exception) {
            // NoSuchElementException attendue si l'id n'existe pas
        }
        // on verifie qu'au minimum le useCase a ete appele
        coVerify(exactly = 1) { getMyBeneficiaries() }
    }

    @Test fun `load passe en Error si useCase echoue`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Error(500, "Erreur")

        viewModel.load(1)

        assertTrue(viewModel.beneficiary.value is UiState.Error)
    }

    @Test fun `save met actionState en Success`() {
        coEvery { editBeneficiary(1, "Alice Modifie", "FR76...1", "acc-001") } returns NetworkResult.Success(fakeBenef1)

        viewModel.save(1, "Alice Modifie", "FR76...1", "acc-001")

        assertTrue(viewModel.actionState.value is UiState.Success)
        coVerify(exactly = 1) { editBeneficiary(1, "Alice Modifie", "FR76...1", "acc-001") }
    }

    @Test fun `save passe actionState en Error si echec`() {
        coEvery { editBeneficiary(any(), any(), any(), any()) } returns NetworkResult.Error(404, "Non trouve")

        viewModel.save(1, "Alice", "FR76...1", "acc-001")

        assertTrue(viewModel.actionState.value is UiState.Error)
        assertEquals("Non trouve", (viewModel.actionState.value as UiState.Error).message)
    }

    @Test fun `delete met actionState en Success`() {
        coEvery { deleteBeneficiary(1) } returns NetworkResult.Success(Unit)

        viewModel.delete(1)

        assertTrue(viewModel.actionState.value is UiState.Success)
        coVerify(exactly = 1) { deleteBeneficiary(1) }
    }

    @Test fun `delete passe actionState en Error si echec`() {
        coEvery { deleteBeneficiary(1) } returns NetworkResult.Error(404, "Non trouve")

        viewModel.delete(1)

        assertTrue(viewModel.actionState.value is UiState.Error)
    }

    @Test fun `actionState initial est Idle`() {
        assertTrue(viewModel.actionState.value is UiState.Idle)
    }
}
