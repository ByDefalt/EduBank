package defalt.featureBank.viewModel

import defalt.domain.entity.account.Account
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.State
import defalt.featureBank.usecase.AdminCreateBankAccountUseCase
import defalt.featureBank.usecase.AdminGetAllAccountsForBankUseCase
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

class AdminCreateBankAccountViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val createBankAccount: AdminCreateBankAccountUseCase = mockk()
    private val getAllAccounts: AdminGetAllAccountsForBankUseCase = mockk()
    private lateinit var viewModel: AdminCreateBankAccountViewModel

    private val fakeAccounts = listOf(
        Account(id = "acc-001"),
        Account(id = "acc-002"),
    )
    private val fakeDetail = BankAccountDetail(id = "bank-001", sold = 0.0, iban = "FR76...")

    @Before fun setUp() {
        coEvery { getAllAccounts() } returns NetworkResult.Success(fakeAccounts)
        viewModel = AdminCreateBankAccountViewModel(createBankAccount, getAllAccounts)
    }

    @Test fun `init charge les comptes en Success`() {
        assertTrue(viewModel.accountsState.value is UiState.Success)
        assertEquals(2, (viewModel.accountsState.value as UiState.Success).data.size)
    }

    @Test fun `retryAccounts recharge la liste`() {
        viewModel.retryAccounts()
        coVerify(exactly = 2) { getAllAccounts() }
        assertTrue(viewModel.accountsState.value is UiState.Success)
    }

    @Test fun `accountsState passe en Error si useCase echoue`() {
        coEvery { getAllAccounts() } returns NetworkResult.Error(500, "Erreur")
        val vm = AdminCreateBankAccountViewModel(createBankAccount, getAllAccounts)
        assertTrue(vm.accountsState.value is UiState.Error)
    }

    @Test fun `create appelle le useCase et callback onSuccess`() {
        var called = false
        coEvery { createBankAccount(any(), any()) } returns NetworkResult.Success(fakeDetail)

        viewModel.create(1, "FR76...", 1, 0.0, 500.0, State.ACTIVE) { called = true }

        coVerify(exactly = 1) { createBankAccount(1, any()) }
        assertTrue(called)
    }

    @Test fun `create passe createState en Error si echec`() {
        coEvery { createBankAccount(any(), any()) } returns NetworkResult.Error(400, "IBAN deja utilise")

        viewModel.create(1, "FR76...", 1, 0.0, 500.0, State.ACTIVE) { }

        assertTrue(viewModel.createState.value is UiState.Error)
        assertEquals("IBAN deja utilise", (viewModel.createState.value as UiState.Error).message)
    }

    @Test fun `create ne appelle pas onSuccess si erreur`() {
        var called = false
        coEvery { createBankAccount(any(), any()) } returns NetworkResult.Error(400, "Erreur")

        viewModel.create(1, "FR76...", 1, 0.0, 500.0, State.ACTIVE) { called = true }

        assertTrue(!called)
    }

    @Test fun `create passe createState en Error si exception`() {
        coEvery { createBankAccount(any(), any()) } returns NetworkResult.Exception(RuntimeException("crash"))

        viewModel.create(1, "FR76...", 1, 0.0, 500.0, State.ACTIVE) { }

        assertTrue(viewModel.createState.value is UiState.Error)
    }
}

