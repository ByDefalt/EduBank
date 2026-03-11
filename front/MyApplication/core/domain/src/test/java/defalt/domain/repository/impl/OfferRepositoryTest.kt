package defalt.domain.repository.impl

import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
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

class OfferRepositoryTest {

    private val dataSource: IOfferRemoteDataSource = mockk()
    private lateinit var repository: OfferRepository

    // Dates fixes pour tests déterministes
    private val today: LocalDate = LocalDate.parse("2025-01-01")
    private val next: LocalDate = LocalDate.parse("2025-02-01")
    private val fakeOffer = Offer(1, "Promo", "Desc", Offer.State.ACTIVE, today, next)

    @Before fun setUp() { repository = OfferRepository(dataSource) }

    @Test fun `getActiveOffers delegue au dataSource`() = runTest {
        coEvery { dataSource.getActiveOffers() } returns NetworkResult.Success(listOf(fakeOffer))
        val result = repository.getActiveOffers()
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        coVerify(exactly = 1) { dataSource.getActiveOffers() }
    }

    @Test fun `getActiveOffers propage Error`() = runTest {
        coEvery { dataSource.getActiveOffers() } returns NetworkResult.Error(500, "err")
        assertTrue(repository.getActiveOffers() is NetworkResult.Error)
    }

    @Test fun `getOffers delegue avec parametres`() = runTest {
        coEvery { dataSource.getOffers(Offer.State.ACTIVE, true) } returns NetworkResult.Success(listOf(fakeOffer))
        val result = repository.getOffers(Offer.State.ACTIVE, true)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.getOffers(Offer.State.ACTIVE, true) }
    }

    @Test fun `getOffers propage Exception`() = runTest {
        coEvery { dataSource.getOffers(any(), any()) } returns NetworkResult.Exception(RuntimeException())
        assertTrue(repository.getOffers(null, null) is NetworkResult.Exception)
    }

    @Test fun `getOfferById delegue l id`() = runTest {
        coEvery { dataSource.getOfferById(1) } returns NetworkResult.Success(fakeOffer)
        val result = repository.getOfferById(1)
        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.id)
        coVerify(exactly = 1) { dataSource.getOfferById(1) }
    }

    @Test fun `getOfferById propage Error 404`() = runTest {
        coEvery { dataSource.getOfferById(any()) } returns NetworkResult.Error(404, "not found")
        assertTrue(repository.getOfferById(1) is NetworkResult.Error)
    }

    @Test fun `createOffer delegue la requete`() = runTest {
        val request = OfferInput(
            title = "T",
            description = "D",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = next,
        )
        coEvery { dataSource.createOffer(request) } returns NetworkResult.Success(fakeOffer)
        val result = repository.createOffer(request)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.createOffer(request) }
    }

    @Test fun `updateOffer delegue id et requete`() = runTest {
        val request = OfferInput(
            title = "T",
            description = "D",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = next,
        )
        coEvery { dataSource.updateOffer(1, request) } returns NetworkResult.Success(fakeOffer)
        val result = repository.updateOffer(1, request)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.updateOffer(1, request) }
    }

    @Test fun `patchOfferState delegue id et requete`() = runTest {
        val request = OfferInput(
            state = OfferInput.State.INACTIVE,
            title = "tre",
            description = "gfdgfg",
            startDate = LocalDate.parse("2025-01-01"),
            endDate = LocalDate.parse("2025-02-01"),
            picturePath = "dfdsfds",
        )
        coEvery { dataSource.updateOffer(1, request) } returns NetworkResult.Success(fakeOffer)
        val result = repository.updateOffer(1, request)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.updateOffer(1, request) }
    }

    @Test fun `deleteOffer delegue l id`() = runTest {
        coEvery { dataSource.deleteOffer(1) } returns NetworkResult.Success(Unit)
        val result = repository.deleteOffer(1)
        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { dataSource.deleteOffer(1) }
    }

    @Test fun `deleteOffer propage Error`() = runTest {
        coEvery { dataSource.deleteOffer(any()) } returns NetworkResult.Error(403, "forbidden")
        val result = repository.deleteOffer(1)
        assertTrue(result is NetworkResult.Error)
        assertEquals(403, (result as NetworkResult.Error).code)
    }
}
