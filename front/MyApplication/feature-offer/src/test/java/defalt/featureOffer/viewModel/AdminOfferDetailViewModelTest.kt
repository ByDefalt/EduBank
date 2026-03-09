package defalt.featureOffer.viewModel

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.featureOffer.usecase.DeleteOfferUseCase
import defalt.featureOffer.usecase.GetOfferByIdUseCase
import defalt.featureOffer.usecase.UpdateOfferUseCase
import defalt.testing.MainDispatcherRule
import defalt.ui.state.UiState
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AdminOfferDetailViewModelTest {

    @get:Rule val dispatcherRule = MainDispatcherRule()

    private val getOfferById: GetOfferByIdUseCase = mockk()
    private val updateOffer: UpdateOfferUseCase = mockk()
    private val deleteOffer: DeleteOfferUseCase = mockk()
    private lateinit var viewModel: AdminOfferDetailViewModel

    private val fakeOffer = Offer(1, "Titre", "Desc", Offer.State.ACTIVE, LocalDate.now(), LocalDate.now().plusMonths(1))

    @Before fun setUp() {
        viewModel = AdminOfferDetailViewModel(getOfferById, updateOffer, deleteOffer)
    }

    @Test fun `load charge l offre en Success`() {
        coEvery { getOfferById(1) } returns NetworkResult.Success(fakeOffer)
        viewModel.load(1)
        assertTrue(viewModel.uiState.value is UiState.Success)
        assertEquals(fakeOffer, (viewModel.uiState.value as UiState.Success).data)
    }

    @Test fun `load passe en Error si le useCase echoue`() {
        coEvery { getOfferById(1) } returns NetworkResult.Error(404, "Non trouvee")
        viewModel.load(1)
        assertTrue(viewModel.uiState.value is UiState.Error)
        assertEquals("Non trouvee", (viewModel.uiState.value as UiState.Error).message)
    }

    @Test fun `save met actionState en Success puis recharge`() {
        coEvery { getOfferById(1) } returns NetworkResult.Success(fakeOffer)
        coEvery { updateOffer(any(), any()) } returns NetworkResult.Success(fakeOffer)

        viewModel.save(
            id = 1,
            title = "Titre",
            description = "Desc",
            state = OffersIdPutRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )

        coVerify(exactly = 1) { updateOffer(1, any()) }
        coVerify(exactly = 1) { getOfferById(1) }
    }

    @Test fun `save met actionState en Error si le useCase echoue`() {
        coEvery { updateOffer(any(), any()) } returns NetworkResult.Error(400, "Invalide")

        viewModel.save(
            id = 1,
            title = "Titre",
            description = "Desc",
            state = OffersIdPutRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )

        assertTrue(viewModel.actionState.value is UiState.Error)
    }

    @Test fun `delete appelle le useCase et callback onSuccess`() {
        var called = false
        coEvery { deleteOffer(1) } returns NetworkResult.Success(Unit)

        viewModel.delete(1) { called = true }

        coVerify(exactly = 1) { deleteOffer(1) }
        assertTrue(called)
    }

    @Test fun `delete ne appelle pas onSuccess si erreur`() {
        var called = false
        coEvery { deleteOffer(1) } returns NetworkResult.Error(500, "Erreur")

        viewModel.delete(1) { called = true }

        assertTrue(viewModel.actionState.value is UiState.Error)
        assertTrue(!called)
    }
}

