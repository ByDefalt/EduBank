package defalt.featureOffer.viewModel

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.domain.session.Session
import defalt.featureOffer.usecase.CreateOfferUseCase
import defalt.featureOffer.usecase.GetAllOffersUseCase
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test



class OffersViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getAllOffers: GetAllOffersUseCase = mockk()
    private lateinit var session: Session
    private lateinit var viewModel: OffersViewModel


    private val today: LocalDate = LocalDate.parse("2025-01-01")
    private val nextMonth: LocalDate = today.plusMonths(1)
    private val nextMonth2: LocalDate = today.plusMonths(2)

    private val fakeOffers = listOf(
        Offer(1, "Promo A", "Desc", Offer.State.ACTIVE, today, nextMonth),
        Offer(2, "Promo B", "Desc", Offer.State.INACTIVE, today, nextMonth2),
    )

    @Before fun setUp() {
        session = Session()
        coEvery { getAllOffers() } returns NetworkResult.Success(fakeOffers)
        viewModel = OffersViewModel(getAllOffers, session)
    }

    @Test fun `init charge les offres en Success`() {
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(2, (viewModel.uiState.value as UiState.Success).data.size)
    }

    @Test fun `retry recharge les offres`() {
        viewModel.retry()
        coVerify(atLeast = 2) { getAllOffers() }
        assertTrue(viewModel.uiState.value is UiState.Success)
    }

    @Test fun `passe en Error si useCase echoue`() {
        coEvery { getAllOffers() } returns NetworkResult.Error(500, "Erreur serveur")
        val vm = OffersViewModel(getAllOffers, session)
        assertTrue(vm.uiState.value is UiState.Error)
        assertEquals("Erreur serveur", (vm.uiState.value as UiState.Error).message)
    }

    @Test fun `passe en Error si exception`() {
        coEvery { getAllOffers() } returns NetworkResult.Exception(RuntimeException("crash"))
        val vm = OffersViewModel(getAllOffers, session)
        assertTrue(vm.uiState.value is UiState.Error)
    }

    @Test fun `isConnected false si pas de token`() {
        session.token = null
        assertFalse(viewModel.isConnected)
    }

    @Test fun `isConnected true si token present`() {
        session.token = "fake-token"
        assertTrue(viewModel.isConnected)
    }

    @Test fun `liste vide en Success`() {
        coEvery { getAllOffers() } returns NetworkResult.Success(emptyList())
        val vm = OffersViewModel(getAllOffers, session)
        assertTrue((vm.uiState.value as UiState.Success).data.isEmpty())
    }
}



class AdminCreateOfferViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val createOffer: CreateOfferUseCase = mockk()
    private lateinit var viewModel: AdminCreateOfferViewModel


    private val today = LocalDate.parse("2025-01-01")
    private val nextMonth = today.plusMonths(1)
    private val fakeOffer = Offer(1, "Promo", "Desc", Offer.State.ACTIVE, today, nextMonth)

    @Before fun setUp() {
        viewModel = AdminCreateOfferViewModel(createOffer)
    }

    @Test fun `uiState initial est Idle`() {
        assertTrue(viewModel.uiState.value is UiState.Idle)
    }

    @Test fun `create passe en Success`() {
        coEvery { createOffer(any()) } returns NetworkResult.Success(fakeOffer)

        viewModel.create("Promo", "Desc", OfferInput.State.ACTIVE, today, nextMonth)

        assertTrue(viewModel.uiState.value is UiState.Success)
        coVerify(exactly = 1) { createOffer(any()) }
    }

    @Test fun `create passe en Error si useCase echoue`() {
        coEvery { createOffer(any()) } returns NetworkResult.Error(400, "Donnees invalides")

        viewModel.create("X", "Y", OfferInput.State.INACTIVE, today, nextMonth)

        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Donnees invalides", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `create passe en Error si exception`() {
        coEvery { createOffer(any()) } returns NetworkResult.Exception(RuntimeException("crash"))

        viewModel.create("X", "Y", OfferInput.State.EXPIRED, today, nextMonth)

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test fun `create transmet les bons parametres`() {
        coEvery { createOffer(any()) } returns NetworkResult.Success(fakeOffer)

        viewModel.create("Mon Titre", "Ma Desc", OfferInput.State.ACTIVE, today, nextMonth)

        coVerify {
            createOffer(
                match {
                    it.title == "Mon Titre" && it.description == "Ma Desc" && it.state == OfferInput.State.ACTIVE
                },
            )
        }
    }

    @Test fun `create avec etat INACTIVE transmet INACTIVE`() {
        coEvery { createOffer(any()) } returns NetworkResult.Success(fakeOffer)

        viewModel.create("T", "D", OfferInput.State.INACTIVE, today, nextMonth)

        coVerify { createOffer(match { it.state == OfferInput.State.INACTIVE }) }
    }
}
