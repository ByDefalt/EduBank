package defalt.featureBank.viewModel

import defalt.domain.entity.bank.BankAccountDetail
import defalt.featureBank.usecase.GetAllMyAccount
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

class ListAccountViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getAllMyAccount: GetAllMyAccount = mockk()
    private lateinit var viewModel: ListAccountViewModel

    private val fakeAccounts = listOf(
        BankAccountDetail(id = "bank-001", sold = 500.0, iban = "FR76...1"),
        BankAccountDetail(id = "bank-002", sold = 1500.0, iban = "FR76...2"),
    )

    @Before fun setUp() {
        coEvery { getAllMyAccount() } returns NetworkResult.Success(fakeAccounts)
        viewModel = ListAccountViewModel(getAllMyAccount)
    }

    @Test fun `init charge les comptes et passe en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, (viewModel.uiState.value as UiState.Success).data.size)
    }

    @Test fun `retry recharge les comptes`() {
        viewModel.retry()
        coVerify(exactly = 2) { getAllMyAccount() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si le useCase echoue`() {
        coEvery { getAllMyAccount() } returns NetworkResult.Error(500, "Erreur")
        val vm = ListAccountViewModel(getAllMyAccount)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `liste vide en Success`() {
        coEvery { getAllMyAccount() } returns NetworkResult.Success(emptyList())
        val vm = ListAccountViewModel(getAllMyAccount)
        assertTrue(vm.uiState.value is UiState.Success)
        assertTrue((vm.uiState.value as UiState.Success).data.isEmpty())
    }

    @Test fun `passe en Error si exception`() {
        coEvery { getAllMyAccount() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = ListAccountViewModel(getAllMyAccount)
        assertTrue(vm.uiState.value is UiState.Error)
    }
}
