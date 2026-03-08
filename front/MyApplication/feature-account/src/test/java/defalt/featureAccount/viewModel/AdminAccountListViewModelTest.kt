package defalt.featureAccount.viewModel

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.featureAccount.usecase.GetAllAccountsUseCase
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

class AdminAccountListViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getAllAccounts: GetAllAccountsUseCase = mockk()
    private lateinit var viewModel: AdminAccountListViewModel

    private val fakeAccounts = listOf(
        Account(id = "acc-001", state = AccountStateEnum.ACTIVE),
        Account(id = "acc-002", state = AccountStateEnum.INACTIVE),
    )

    @Before
    fun setUp() {
        coEvery { getAllAccounts() } returns NetworkResult.Success(fakeAccounts)
        viewModel = AdminAccountListViewModel(getAllAccounts)
    }

    @Test
    fun `init charge la liste et passe en Success`() {
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(2, (state as UiState.Success).data.size)
    }

    @Test
    fun `retry recharge la liste`() {
        coEvery { getAllAccounts() } returns NetworkResult.Success(fakeAccounts)

        viewModel.retry()

        coVerify(exactly = 2) { getAllAccounts() } // init + retry
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test
    fun `passe en Error si le useCase echoue`() {
        coEvery { getAllAccounts() } returns NetworkResult.Error(500, "Erreur serveur")
        val vm = AdminAccountListViewModel(getAllAccounts)

        assertTrue(vm.uiState.value is UiState.Error)
        assertEquals("Erreur serveur", (vm.uiState.value as UiState.Error).message)
    }

    @Test
    fun `passe en Error si une exception est levee`() {
        coEvery { getAllAccounts() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = AdminAccountListViewModel(getAllAccounts)

        assertTrue(vm.uiState.value is UiState.Error)
    }
}
