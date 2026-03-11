package defalt.featureOperation.viewModel

import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.usecase.CancelOperationUseCase
import defalt.featureOperation.usecase.GetOperationByIdUseCase
import defalt.featureOperation.usecase.UpdateOperationStateUseCase
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
import java.time.OffsetDateTime

class AdminOperationDetailViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getOperationById: GetOperationByIdUseCase = mockk()
    private val cancelOperation: CancelOperationUseCase = mockk()
    private val updateOperationState: UpdateOperationStateUseCase = mockk()
    private lateinit var viewModel: AdminOperationDetailViewModel

    private val fakeOperation = Operation(
        id = 1,
        accountSourceId = "acc-001",
        label = "Virement test",
        state = OperationState.PENDING,
        ibanTarget = "FR76...",
        amount = 100.0,
        date = OffsetDateTime.now(),
    )

    @Before fun setUp() {
        viewModel = AdminOperationDetailViewModel(getOperationById, cancelOperation, updateOperationState)
    }

    @Test fun `load charge l operation en Success`() {
        coEvery { getOperationById(1) } returns NetworkResult.Success(fakeOperation)
        viewModel.load(1)
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(fakeOperation, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test fun `load passe en Error`() {
        coEvery { getOperationById(1) } returns NetworkResult.Error(404, "Non trouvee")
        viewModel.load(1)
        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Non trouvee", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `cancel annule l operation et recharge`() {
        coEvery { getOperationById(1) } returns NetworkResult.Success(fakeOperation.copy(state = OperationState.CANCELLED))
        coEvery { cancelOperation(1) } returns NetworkResult.Success(fakeOperation.copy(state = OperationState.CANCELLED))

        viewModel.cancel(1)

        coVerify(exactly = 1) { cancelOperation(1) }
        coVerify(exactly = 1) { getOperationById(1) }
    }

    @Test fun `cancel met actionState en Error si le useCase echoue`() {
        coEvery { cancelOperation(1) } returns NetworkResult.Error(400, "Impossible d annuler")

        viewModel.cancel(1)

        assertTrue(viewModel.actionState.value is UiState.Error)
    }

    @Test fun `updateState met a jour l etat et recharge`() {
        coEvery { getOperationById(1) } returns NetworkResult.Success(fakeOperation.copy(state = OperationState.COMPLETED))
        coEvery { updateOperationState(1, OperationState.COMPLETED) } returns NetworkResult.Success(
            fakeOperation.copy(state = OperationState.COMPLETED),
        )

        viewModel.updateState(1, OperationState.COMPLETED)

        coVerify(exactly = 1) { updateOperationState(1, OperationState.COMPLETED) }
        coVerify(exactly = 1) { getOperationById(1) }
    }

    @Test fun `updateState met actionState en Error si le useCase echoue`() {
        coEvery { updateOperationState(1, any()) } returns NetworkResult.Error(422, "Etat invalide")

        viewModel.updateState(1, OperationState.COMPLETED)

        assertTrue(viewModel.actionState.value is UiState.Error)
    }
}
