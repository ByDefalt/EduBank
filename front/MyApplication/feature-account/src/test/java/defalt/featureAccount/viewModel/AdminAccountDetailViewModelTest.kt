package defalt.featureAccount.viewModel

import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.domain.entity.account.PersonalInformation
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.usecase.ActivateAccountUseCase
import defalt.featureAccount.usecase.DeactivateAccountUseCase
import defalt.featureAccount.usecase.GetAccountByIdUseCase
import defalt.featureAccount.usecase.UpdatePersonalInfoUseCase
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

class AdminAccountDetailViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getAccountById: GetAccountByIdUseCase = mockk()
    private val activateAccount: ActivateAccountUseCase = mockk()
    private val deactivateAccount: DeactivateAccountUseCase = mockk()
    private val updatePersonalInfo: UpdatePersonalInfoUseCase = mockk()

    private lateinit var viewModel: AdminAccountDetailViewModel

    private val fakeAccount = Account(id = "acc-001", state = AccountStateEnum.ACTIVE)
    private val fakeInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")
    private val fakeData = AccountWithInfo(fakeAccount, fakeInfo)

    @Before
    fun setUp() {
        viewModel = AdminAccountDetailViewModel(getAccountById, activateAccount, deactivateAccount, updatePersonalInfo)
    }

    @Test
    fun `load passe en Success avec les donnees`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Success(fakeData)

        viewModel.load("acc-001")

        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(fakeData, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test
    fun `load passe en Error si le useCase echoue`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Error(404, "Non trouve")

        viewModel.load("acc-001")

        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Non trouve", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test
    fun `activate met actionState en Success puis recharge`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Success(fakeData)
        coEvery { activateAccount("acc-001") } returns NetworkResult.Success(true)

        viewModel.activate("acc-001")

        coVerify(exactly = 1) { activateAccount("acc-001") }
        coVerify(exactly = 1) { getAccountById("acc-001") } // reload
    }

    @Test
    fun `activate met actionState en Error si le useCase echoue`() {
        coEvery { activateAccount("acc-001") } returns NetworkResult.Error(400, "Deja actif")

        viewModel.activate("acc-001")

        assertTrue(viewModel.actionState.value is UiState.Error)
    }

    @Test
    fun `deactivate met actionState en Success puis recharge`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Success(fakeData)
        coEvery { deactivateAccount("acc-001") } returns NetworkResult.Success(true)

        viewModel.deactivate("acc-001")

        coVerify(exactly = 1) { deactivateAccount("acc-001") }
        coVerify(exactly = 1) { getAccountById("acc-001") }
    }

    @Test
    fun `updateInfo ne fait rien si uiState n est pas Success`() {
        // uiState est Loading par défaut — updateInfo doit être ignoré
        viewModel.updateInfo("acc-001", fakeInfo)

        coVerify(exactly = 0) { updatePersonalInfo(any(), any()) }
    }

    @Test
    fun `updateInfo appelle le useCase avec l id de personalInfo`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Success(fakeData)
        coEvery { updatePersonalInfo(1, any()) } returns NetworkResult.Success(fakeInfo)

        viewModel.load("acc-001")
        viewModel.updateInfo("acc-001", fakeInfo.copy(firstname = "Bob"))

        coVerify(exactly = 1) { updatePersonalInfo(1, any()) }
    }

    @Test
    fun `updateInfo recharge apres succes`() {
        coEvery { getAccountById("acc-001") } returns NetworkResult.Success(fakeData)
        coEvery { updatePersonalInfo(1, any()) } returns NetworkResult.Success(fakeInfo)

        viewModel.load("acc-001")
        viewModel.updateInfo("acc-001", fakeInfo)

        coVerify(exactly = 2) { getAccountById("acc-001") } // load + reload après update
    }
}
