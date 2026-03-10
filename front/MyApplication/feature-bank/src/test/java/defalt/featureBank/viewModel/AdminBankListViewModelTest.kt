package defalt.featureBank.viewModel

import defalt.domain.entity.bank.BankAccount
import defalt.featureBank.usecase.AdminGetAllBankAccountsUseCase
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

class AdminBankListViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getAllBankAccounts: AdminGetAllBankAccountsUseCase = mockk()
    private lateinit var viewModel: AdminBankListViewModel

    private val fakeAccounts = listOf(
        BankAccount(id = "bank-001", parameterId = 1, typeId = 1, sold = 500.0, iban = "FR76...1"),
        BankAccount(id = "bank-002", parameterId = 2, typeId = 2, sold = 1500.0, iban = "FR76...2"),
    )

    @Before fun setUp() {
        coEvery { getAllBankAccounts() } returns NetworkResult.Success(fakeAccounts)
        viewModel = AdminBankListViewModel(getAllBankAccounts)
    }

    @Test fun `init charge la liste en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, (viewModel.uiState.value as UiState.Success).data.size)
    }

    @Test fun `retry recharge la liste`() {
        viewModel.retry()
        coVerify(exactly = 2) { getAllBankAccounts() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si useCase echoue`() {
        coEvery { getAllBankAccounts() } returns NetworkResult.Error(500, "Erreur")
        val vm = AdminBankListViewModel(getAllBankAccounts)
        assertTrue(vm.uiState.value is UiState.Error)
        assertEquals("Erreur", (vm.uiState.value as UiState.Error).message)
    }

    @Test fun `passe en Error si exception`() {
        coEvery { getAllBankAccounts() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = AdminBankListViewModel(getAllBankAccounts)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `liste vide en Success`() {
        coEvery { getAllBankAccounts() } returns NetworkResult.Success(emptyList())
        val vm = AdminBankListViewModel(getAllBankAccounts)
        assertTrue(vm.uiState.value is UiState.Success)
        assertTrue((vm.uiState.value as UiState.Success).data.isEmpty())
    }
}
