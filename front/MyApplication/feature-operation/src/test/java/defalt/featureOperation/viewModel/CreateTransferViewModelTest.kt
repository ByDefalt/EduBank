package defalt.featureOperation.viewModel

import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Beneficiary
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.usecase.CreateTransfer
import defalt.featureOperation.usecase.GetMyBankAccounts
import defalt.featureOperation.usecase.GetMyBeneficiaries
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.OffsetDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateTransferViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getMyBankAccounts: GetMyBankAccounts = mockk()
    private val getMyBeneficiaries: GetMyBeneficiaries = mockk()
    private val createTransfer: CreateTransfer = mockk()
    private lateinit var viewModel: CreateTransferViewModel

    private val fakeBankAccounts = listOf(
        BankAccount(id = "bank-001", parameterId = 1, typeId = 1, sold = 1000.0, iban = "FR76...1"),
        BankAccount(id = "bank-002", parameterId = 2, typeId = 2, sold = 500.0, iban = "FR76...2"),
    )
    private val fakeBeneficiaries = listOf(
        Beneficiary(id = 1, accountSourceId = "acc-001", ibanTarget = "FR76...3", name = "Alice"),
    )
    private val fakeOperation = Operation(
        id = 1, accountSourceId = "acc-001", label = "Virement",
        state = OperationState.PENDING, ibanTarget = "FR76...3", amount = 50.0, date = OffsetDateTime.now(),
    )

    @Before fun setUp() {
        coEvery { getMyBankAccounts() } returns NetworkResult.Success(fakeBankAccounts)
        coEvery { getMyBeneficiaries() } returns NetworkResult.Success(fakeBeneficiaries)
        viewModel = CreateTransferViewModel(getMyBankAccounts, getMyBeneficiaries, createTransfer)
    }

    // ── Init ────────────────────────────────────────────────────────────────

    @Test fun `init charge debitUiState en Success`() {
        assertTrue(viewModel.debitUiState.value is UiState.Success)
        assertEquals(2, (viewModel.debitUiState.value as UiState.Success).data.accounts.size)
    }

    @Test fun `init charge receiverUiState en Success`() {
        assertTrue(viewModel.receiverUiState.value is UiState.Success)
        assertEquals(1, (viewModel.receiverUiState.value as UiState.Success).data.beneficiaries.size)
    }

    @Test fun `debitUiState passe en Error si getMyBankAccounts echoue`() {
        coEvery { getMyBankAccounts() } returns NetworkResult.Error(500, "Erreur")
        val vm = CreateTransferViewModel(getMyBankAccounts, getMyBeneficiaries, createTransfer)
        assertTrue(vm.debitUiState.value is UiState.Error)
    }

    @Test fun `receiverUiState passe en Error si getMyBeneficiaries echoue`() {
        coEvery { getMyBeneficiaries() } returns NetworkResult.Error(500, "Erreur")
        val vm = CreateTransferViewModel(getMyBankAccounts, getMyBeneficiaries, createTransfer)
        assertTrue(vm.receiverUiState.value is UiState.Error)
    }

    // ── Form ────────────────────────────────────────────────────────────────

    @Test fun `formulaire initial est vide`() {
        assertNull(viewModel.form.value.sourceAccount)
        assertNull(viewModel.form.value.receiverIban)
        assertEquals("", viewModel.form.value.amount)
        assertEquals("", viewModel.form.value.label)
    }

    @Test fun `selectSourceAccount met a jour le formulaire`() {
        viewModel.selectSourceAccount(fakeBankAccounts[0])
        assertEquals(fakeBankAccounts[0], viewModel.form.value.sourceAccount)
    }

    @Test fun `selectSourceAccount remplace l ancien compte`() {
        viewModel.selectSourceAccount(fakeBankAccounts[0])
        viewModel.selectSourceAccount(fakeBankAccounts[1])
        assertEquals(fakeBankAccounts[1], viewModel.form.value.sourceAccount)
    }

    // ── Retry ───────────────────────────────────────────────────────────────

    @Test fun `retryDebit recharge les comptes`() {
        viewModel.retryDebit()
        coVerify(atLeast = 2) { getMyBankAccounts() } // init + retry
    }

    @Test fun `retryReceiver recharge les donnees`() {
        viewModel.retryReceiver()
        // getMyBankAccounts et getMyBeneficiaries appeles au moins 2 fois chacun
        coVerify(atLeast = 2) { getMyBeneficiaries() }
    }

    // ── Submit ──────────────────────────────────────────────────────────────

    private fun fillFormAndSubmit(amount: Double = 50.0, label: String = "Virement") {
        viewModel.selectReceiverBeneficiary(fakeBeneficiaries[0])
        viewModel.setAmount(amount.toString())
        viewModel.setLabel(label)
        viewModel.submitTransfer()
    }

    @Test fun `submitTransfer passe en Success et appelle createTransfer`() {
        coEvery { createTransfer("FR76...3", 50.0, "Virement") } returns NetworkResult.Success(fakeOperation)

        fillFormAndSubmit(50.0, "Virement")

        assertTrue(viewModel.submitUiState.value is UiState.Success)
        coVerify(exactly = 1) { createTransfer("FR76...3", 50.0, "Virement") }
    }

    @Test fun `submitTransfer passe en Error si createTransfer echoue`() {
        coEvery { createTransfer(any(), any(), any()) } returns NetworkResult.Error(400, "Solde insuffisant")

        fillFormAndSubmit(9999.0)

        assertTrue(viewModel.submitUiState.value is UiState.Error)
        assertEquals("Solde insuffisant", (viewModel.submitUiState.value as UiState.Error).message)
    }

    @Test fun `submitTransfer passe en Error si exception reseau`() {
        coEvery { createTransfer(any(), any(), any()) } returns NetworkResult.Exception(RuntimeException("crash"))

        fillFormAndSubmit()

        assertTrue(viewModel.submitUiState.value is UiState.Error)
    }

    @Test fun `submitUiState initial est Idle`() {
        assertTrue(viewModel.submitUiState.value is UiState.Idle)
    }

    @Test fun `reset remet le formulaire et submitUiState a Idle`() {
        coEvery { createTransfer(any(), any(), any()) } returns NetworkResult.Success(fakeOperation)
        fillFormAndSubmit()
        assertTrue(viewModel.submitUiState.value is UiState.Success)

        viewModel.reset()

        assertTrue(viewModel.submitUiState.value is UiState.Idle)
        assertNull(viewModel.form.value.sourceAccount)
        assertNull(viewModel.form.value.receiverIban)
    }

    @Test fun `setAmount met a jour le formulaire`() {
        viewModel.setAmount("150.0")
        assertEquals("150.0", viewModel.form.value.amount)
    }

    @Test fun `setLabel met a jour le formulaire`() {
        viewModel.setLabel("Mon virement")
        assertEquals("Mon virement", viewModel.form.value.label)
    }

    @Test fun `selectReceiverBeneficiary met a jour iban et nom`() {
        viewModel.selectReceiverBeneficiary(fakeBeneficiaries[0])
        assertEquals("FR76...3", viewModel.form.value.receiverIban)
        assertEquals("Alice", viewModel.form.value.receiverName)
    }

    @Test fun `selectReceiverAccount met a jour iban`() {
        viewModel.selectReceiverAccount(fakeBankAccounts[0])
        assertEquals("FR76...1", viewModel.form.value.receiverIban)
    }
}

