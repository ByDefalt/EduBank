package defalt.featureOperation.viewModel

import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.usecase.GetAllOperationsUseCase
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

class AdminOperationListViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getAllOperations: GetAllOperationsUseCase = mockk()
    private lateinit var viewModel: AdminOperationListViewModel

    private val fakeOps = listOf(
        Operation(1, "acc-001", "Virement A", OperationState.COMPLETED, "FR76...1", 100.0, OffsetDateTime.now()),
        Operation(2, "acc-001", "Virement B", OperationState.PENDING, "FR76...2", 50.0, OffsetDateTime.now()),
    )

    @Before fun setUp() {
        coEvery { getAllOperations() } returns NetworkResult.Success(fakeOps)
        viewModel = AdminOperationListViewModel(getAllOperations)
    }

    @Test fun `init charge les operations en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, (viewModel.uiState.value as UiState.Success).data.size)
    }

    @Test fun `retry recharge les operations`() {
        coEvery { getAllOperations() } returns NetworkResult.Success(fakeOps)
        viewModel.retry()
        coVerify(exactly = 2) { getAllOperations() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si le useCase echoue`() {
        coEvery { getAllOperations() } returns NetworkResult.Error(500, "Erreur")
        val vm = AdminOperationListViewModel(getAllOperations)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `passe en Error si une exception est levee`() {
        coEvery { getAllOperations() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = AdminOperationListViewModel(getAllOperations)
        assertTrue(vm.uiState.value is UiState.Error)
    }
}
