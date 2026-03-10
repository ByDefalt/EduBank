package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.repository.service.IOfferRepository
import defalt.testing.FakeLogger
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// ── GetAllOffersUseCase ──────────────────────────────────────────────────────

class GetAllOffersUseCaseTest {
    private val repository: IOfferRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: GetAllOffersUseCase

    private val fakeOffers = listOf(
        Offer(1, "Offre 1", "Desc", Offer.State.ACTIVE, LocalDate.now(), LocalDate.now().plusMonths(1)),
        Offer(2, "Offre 2", "Desc", Offer.State.INACTIVE, LocalDate.now(), LocalDate.now().plusMonths(2)),
    )

    @Before fun setUp() { useCase = GetAllOffersUseCase(repository, logger) }

    @Test fun `retourne la liste des offres`() = runTest {
        coEvery { repository.getOffers() } returns NetworkResult.Success(fakeOffers)
        val result = useCase()
        assertTrue(result is NetworkResult.Success)
        assertEquals(2, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { repository.getOffers() }
    }

    @Test fun `retourne liste vide`() = runTest {
        coEvery { repository.getOffers() } returns NetworkResult.Success(emptyList())
        val result = useCase()
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `propage l erreur`() = runTest {
        coEvery { repository.getOffers() } returns NetworkResult.Error(500, "Erreur")
        assertTrue(useCase() is NetworkResult.Error)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.getOffers() } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase() is NetworkResult.Exception)
    }
}

// ── CreateOfferUseCase ───────────────────────────────────────────────────────

class CreateOfferUseCaseTest {
    private val repository: IOfferRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: CreateOfferUseCase

    @Before fun setUp() { useCase = CreateOfferUseCase(repository, logger) }

    @Test fun `cree une offre avec succes`() = runTest {
        val request = OffersPostRequest(
            title = "Titre",
            description = "Desc",
            state = OffersPostRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )
        val offer = Offer(1, "Titre", "Desc", Offer.State.ACTIVE, LocalDate.now(), LocalDate.now().plusMonths(1))
        coEvery { repository.createOffer(request) } returns NetworkResult.Success(offer)

        val result = useCase(request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Titre", (result as NetworkResult.Success).data.title)
        coVerify(exactly = 1) { repository.createOffer(request) }
    }

    @Test fun `propage l erreur de creation`() = runTest {
        val request = OffersPostRequest(
            title = "Titre",
            description = "Desc",
            state = OffersPostRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )
        coEvery { repository.createOffer(request) } returns NetworkResult.Error(400, "Donnees invalides")

        val result = useCase(request)

        assertTrue(result is NetworkResult.Error)
        assertEquals("Donnees invalides", (result as NetworkResult.Error).message)
    }

    @Test fun `propage l exception`() = runTest {
        val request = OffersPostRequest(
            title = "T",
            description = "D",
            state = OffersPostRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )
        coEvery { repository.createOffer(request) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase(request) is NetworkResult.Exception)
    }
}

// ── UpdateOfferUseCase ───────────────────────────────────────────────────────

class UpdateOfferUseCaseTest {
    private val repository: IOfferRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: UpdateOfferUseCase

    @Before fun setUp() { useCase = UpdateOfferUseCase(repository, logger) }

    @Test fun `met a jour l offre avec succes`() = runTest {
        val request = OffersIdPutRequest(
            title = "Nouveau titre",
            description = "Desc",
            state = OffersIdPutRequest.State.ACTIVE,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusMonths(1),
        )
        val offer = Offer(1, "Nouveau titre", "Desc", Offer.State.ACTIVE, LocalDate.now(), LocalDate.now().plusMonths(1))
        coEvery { repository.updateOffer(1, request) } returns NetworkResult.Success(offer)

        val result = useCase(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Nouveau titre", (result as NetworkResult.Success).data.title)
    }

    @Test fun `propage l erreur de mise a jour`() = runTest {
        val request = OffersIdPutRequest(
            title = "Titre",
            state = OffersIdPutRequest.State.INACTIVE,
        )
        coEvery { repository.updateOffer(1, request) } returns NetworkResult.Error(404, "Non trouvee")

        val result = useCase(1, request)

        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        val request = OffersIdPutRequest(title = "T")
        coEvery { repository.updateOffer(1, request) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(useCase(1, request) is NetworkResult.Exception)
    }
}

// ── DeleteOfferUseCase ───────────────────────────────────────────────────────

class DeleteOfferUseCaseTest {
    private val repository: IOfferRepository = mockk()
    private val logger = FakeLogger()
    private lateinit var useCase: DeleteOfferUseCase

    @Before fun setUp() { useCase = DeleteOfferUseCase(repository, logger) }

    @Test fun `supprime l offre avec succes`() = runTest {
        coEvery { repository.deleteOffer(1) } returns NetworkResult.Success(Unit)

        val result = useCase(1)

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { repository.deleteOffer(1) }
    }

    @Test fun `propage l erreur de suppression`() = runTest {
        coEvery { repository.deleteOffer(1) } returns NetworkResult.Error(403, "Interdit")

        val result = useCase(1)

        assertTrue(result is NetworkResult.Error)
        assertEquals(403, (result as NetworkResult.Error).code)
    }

    @Test fun `propage l exception`() = runTest {
        coEvery { repository.deleteOffer(1) } returns NetworkResult.Exception(RuntimeException("crash"))
        assertTrue(useCase(1) is NetworkResult.Exception)
    }
}
