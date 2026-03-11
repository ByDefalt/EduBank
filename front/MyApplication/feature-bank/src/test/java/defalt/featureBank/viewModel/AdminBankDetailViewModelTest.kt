package defalt.featureBank.viewModel

import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.State
import defalt.featureBank.usecase.AdminDeleteBankAccountUseCase
import defalt.featureBank.usecase.AdminGetBankAccountByIdUseCase
import defalt.featureBank.usecase.AdminGetBankAccountTypesUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountParamUseCase
import defalt.featureBank.usecase.AdminUpdateBankAccountUseCase
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

class AdminBankDetailViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getBankAccountById: AdminGetBankAccountByIdUseCase = mockk()
    private val deleteBankAccount: AdminDeleteBankAccountUseCase = mockk()
    private val updateBankAccountParam: AdminUpdateBankAccountParamUseCase = mockk()
    private val updateBankAccount: AdminUpdateBankAccountUseCase = mockk()

    private val getBankAccountType: AdminGetBankAccountTypesUseCase = mockk()

    private lateinit var viewModel: AdminBankDetailViewModel

    private val fakeDetail = BankAccountDetail(id = "bank-001", sold = 1000.0, iban = "FR76...")

    @Before fun setUp() {
        viewModel = AdminBankDetailViewModel(getBankAccountById, deleteBankAccount, updateBankAccountParam, updateBankAccount, getBankAccountType)
    }

    @Test fun `load charge le compte en Success`() {
        coEvery { getBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetail)
        viewModel.load("bank-001")
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(fakeDetail, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test fun `load passe en Error`() {
        coEvery { getBankAccountById("bank-001") } returns NetworkResult.Error(404, "Non trouve")
        viewModel.load("bank-001")
        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Non trouve", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `load passe en Error si exception`() {
        coEvery { getBankAccountById("bank-001") } returns NetworkResult.Exception(RuntimeException("crash"))
        viewModel.load("bank-001")
        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test fun `update met a jour le parametre et recharge`() {
        coEvery { getBankAccountById("bank-001") } returns NetworkResult.Success(fakeDetail)
        coEvery { updateBankAccountParam("bank-001", any()) } returns NetworkResult.Success(Unit)
        viewModel.updateFull("bank-001", 0, 500.0, State.ACTIVE)
        coVerify(exactly = 1) { updateBankAccountParam("bank-001", any()) }
        coVerify(exactly = 1) { getBankAccountById("bank-001") }
    }

    @Test fun `update passe actionState en Error si echec`() {
        coEvery { updateBankAccountParam("bank-001", any()) } returns NetworkResult.Error(400, "Invalide")
        viewModel.updateFull("bank-001", 0, 500.0, State.ACTIVE)
        assertTrue(viewModel.actionState.value is UiState.Error)
    }

    @Test fun `updateFull met a jour le compte complet`() {
        coEvery { updateBankAccount("bank-001", any(), any()) } returns NetworkResult.Success(Unit)
        viewModel.updateFull("bank-001", 1, 500.0, State.ACTIVE)
        coVerify(exactly = 1) { updateBankAccount("bank-001", 1, any()) }
    }

    @Test fun `delete appelle le useCase et callback onSuccess`() {
        var called = false
        coEvery { deleteBankAccount("bank-001") } returns NetworkResult.Success(Unit)
        viewModel.delete("bank-001") { called = true }
        coVerify(exactly = 1) { deleteBankAccount("bank-001") }
        assertTrue(called)
    }

    @Test fun `delete ne appelle pas onSuccess si erreur`() {
        var called = false
        coEvery { deleteBankAccount("bank-001") } returns NetworkResult.Error(500, "Erreur")
        viewModel.delete("bank-001") { called = true }
        assertTrue(viewModel.actionState.value is UiState.Error)
        assertTrue(!called)
    }
}
