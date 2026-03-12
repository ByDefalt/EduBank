package defalt.featureBank.viewModel

import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.bank.BankAccountDetail
import defalt.featureBank.usecase.GetHomeData
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

class HomeAccountViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getHomeData: GetHomeData = mockk()
    private lateinit var viewModel: HomeAccountViewModel

    private val fakeDetail = BankAccountDetail(id = "bank-001", sold = 1200.0, iban = "FR76...")
    private val fakeInfo = PersonalInformation(id = 1, firstname = "Alice", lastname = "Dupont", email = "alice@mail.fr")
    private val fakeHomeData = HomeData(account = fakeDetail, personalInformation = fakeInfo)

    @Before fun setUp() {
        coEvery { getHomeData() } returns NetworkResult.Success(fakeHomeData)
        viewModel = HomeAccountViewModel(getHomeData)
    }

    @Test fun `init charge les donnees et passe en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        val data = (viewModel.uiState.value as UiState.Success).data
        assertEquals(fakeDetail, data.account)
        assertEquals(fakeInfo, data.personalInformation)
    }

    @Test fun `retry recharge les donnees`() {
        viewModel.retry()
        coVerify(exactly = 2) { getHomeData() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si le useCase echoue`() {
        coEvery { getHomeData() } returns NetworkResult.Error(500, "Erreur serveur")
        val vm = HomeAccountViewModel(getHomeData)
        assertTrue(vm.uiState.value is UiState.Error)
        assertEquals("Erreur serveur", (vm.uiState.value as UiState.Error).message)
    }

    @Test fun `passe en Error si une exception est levee`() {
        coEvery { getHomeData() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = HomeAccountViewModel(getHomeData)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `retry apres erreur recharge correctement`() {
        coEvery { getHomeData() } returns NetworkResult.Error(500, "Erreur")
        val vm = HomeAccountViewModel(getHomeData)
        assertTrue(vm.uiState.value is UiState.Error)

        coEvery { getHomeData() } returns NetworkResult.Success(fakeHomeData)
        vm.retry()
        assertTrue(vm.uiState.value is UiState.Success)
    }
}
