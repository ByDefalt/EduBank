package defalt.network.datasource.offer

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.network.api.offer.model.Offer as OfferDto
import defalt.network.api.offer.service.OfferApi
import defalt.utils.NetworkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OfferRemoteDataSourceTest {

    private val offerApi: OfferApi = mockk()
    private lateinit var dataSource: OfferRemoteDataSource

    // Dates fixes pour tests déterministes
    private val today: LocalDate = LocalDate.parse("2025-01-01")
    private val nextMonth: LocalDate = LocalDate.parse("2025-02-01")

    private val fakeOfferDto = OfferDto(
        id = 1,
        title = "Promo",
        description = "Desc",
        state = OfferDto.State.ACTIVE,
        startDate = today,
        endDate = nextMonth,
    )

    @Before fun setUp() {
        dataSource = OfferRemoteDataSource(offerApi)
    }

    // ── getActiveOffers ──────────────────────────────────────────────────────

    @Test fun `getActiveOffers retourne la liste en succes`() = runTest {
        coEvery { offerApi.offersActiveGet() } returns Response.success(listOf(fakeOfferDto))

        val result = dataSource.getActiveOffers()

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
        assertEquals("Promo", result.data[0].title)
    }

    @Test fun `getActiveOffers retourne liste vide`() = runTest {
        coEvery { offerApi.offersActiveGet() } returns Response.success(emptyList())

        val result = dataSource.getActiveOffers()

        assertTrue(result is NetworkResult.Success)
        assertTrue((result as NetworkResult.Success).data.isEmpty())
    }

    @Test fun `getActiveOffers propage Error`() = runTest {
        coEvery { offerApi.offersActiveGet() } returns Response.error(500, "error".toResponseBody())
        assertTrue(dataSource.getActiveOffers() is NetworkResult.Error)
    }

    @Test fun `getActiveOffers propage Exception`() = runTest {
        coEvery { offerApi.offersActiveGet() } throws RuntimeException("crash")
        assertTrue(dataSource.getActiveOffers() is NetworkResult.Exception)
    }

    // ── getOffers ───────────────────────────────────────────────────────────

    @Test fun `getOffers retourne la liste en succes`() = runTest {
        coEvery { offerApi.offersGet(any(), any()) } returns Response.success(listOf(fakeOfferDto))

        val result = dataSource.getOffers(null, null)

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.size)
    }

    @Test fun `getOffers filtre ACTIVE mappe vers dto`() = runTest {
        coEvery { offerApi.offersGet(OfferApi.StateOffersGet.ACTIVE, null) } returns
            Response.success(listOf(fakeOfferDto))

        dataSource.getOffers(Offer.State.ACTIVE, null)

        coVerify { offerApi.offersGet(OfferApi.StateOffersGet.ACTIVE, null) }
    }

    @Test fun `getOffers filtre INACTIVE mappe vers dto`() = runTest {
        coEvery { offerApi.offersGet(OfferApi.StateOffersGet.INACTIVE, null) } returns
            Response.success(emptyList())

        dataSource.getOffers(Offer.State.INACTIVE, null)

        coVerify { offerApi.offersGet(OfferApi.StateOffersGet.INACTIVE, null) }
    }

    @Test fun `getOffers filtre EXPIRED mappe vers dto`() = runTest {
        coEvery { offerApi.offersGet(OfferApi.StateOffersGet.EXPIRED, null) } returns
            Response.success(emptyList())

        dataSource.getOffers(Offer.State.EXPIRED, null)

        coVerify { offerApi.offersGet(OfferApi.StateOffersGet.EXPIRED, null) }
    }

    @Test fun `getOffers passe activeOnly au api`() = runTest {
        coEvery { offerApi.offersGet(null, true) } returns Response.success(listOf(fakeOfferDto))

        dataSource.getOffers(null, true)

        coVerify { offerApi.offersGet(null, true) }
    }

    @Test fun `getOffers propage Error 401`() = runTest {
        coEvery { offerApi.offersGet(any(), any()) } returns
            Response.error(401, "unauthorized".toResponseBody())

        assertTrue(dataSource.getOffers(null, null) is NetworkResult.Error)
    }

    // ── getOfferById ────────────────────────────────────────────────────────

    @Test fun `getOfferById retourne l offre en succes`() = runTest {
        coEvery { offerApi.offersIdGet(1) } returns Response.success(fakeOfferDto)

        val result = dataSource.getOfferById(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(1, (result as NetworkResult.Success).data.id)
        assertEquals("Promo", result.data.title)
    }

    @Test fun `getOfferById propage Error 404`() = runTest {
        coEvery { offerApi.offersIdGet(1) } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.getOfferById(1) is NetworkResult.Error)
    }

    @Test fun `getOfferById propage Exception`() = runTest {
        coEvery { offerApi.offersIdGet(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.getOfferById(1) is NetworkResult.Exception)
    }

    // ── createOffer ──────────────────────────────────────────────────────────

    @Test fun `createOffer retourne l offre creee`() = runTest {
        coEvery { offerApi.offersPost(any()) } returns Response.success(fakeOfferDto)

        val request = OfferInput(
            title = "Promo",
            description = "Desc",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = nextMonth,
        )
        val result = dataSource.createOffer(request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Promo", (result as NetworkResult.Success).data.title)
        coVerify(exactly = 1) { offerApi.offersPost(any()) }
    }

    @Test fun `createOffer propage Error 400`() = runTest {
        coEvery { offerApi.offersPost(any()) } returns Response.error(400, "invalid".toResponseBody())

        val request = OfferInput(
            title = "T",
            description = "D",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = nextMonth,
        )
        assertTrue(dataSource.createOffer(request) is NetworkResult.Error)
    }

    // ── updateOffer ──────────────────────────────────────────────────────────

    @Test fun `updateOffer retourne l offre mise a jour`() = runTest {
        val updated = fakeOfferDto.copy(title = "Promo Modif")
        coEvery { offerApi.offersIdPut(1, any()) } returns Response.success(updated)

        val request = OfferInput(
            title = "Promo Modif",
            description = "Desc",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = nextMonth,
        )
        val result = dataSource.updateOffer(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals("Promo Modif", (result as NetworkResult.Success).data.title)
    }

    @Test fun `updateOffer propage Error 404`() = runTest {
        coEvery { offerApi.offersIdPut(any(), any()) } returns Response.error(404, "not found".toResponseBody())

        val request = OfferInput(
            title = "T", description = "D",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = nextMonth,
            picturePath = "ffdfds"
        )
        assertTrue(dataSource.updateOffer(1, request) is NetworkResult.Error)
    }

    // ── patchOfferState ──────────────────────────────────────────────────────

    @Test fun `patchOfferState retourne l offre modifiee`() = runTest {
        val patched = fakeOfferDto.copy(state = OfferDto.State.INACTIVE)
        // La méthode patchOfferState fait désormais un GET puis un PUT
        coEvery { offerApi.offersIdGet(1) } returns Response.success(fakeOfferDto)
        coEvery { offerApi.offersIdPut(1, any()) } returns Response.success(patched)

        val request = OfferInput(
            title = "T", description = "D",
            state = OfferInput.State.INACTIVE,
            startDate = today,
            endDate = nextMonth,
            picturePath = "ffdfds"
        )
        val result = dataSource.updateOffer(1, request)

        assertTrue(result is NetworkResult.Success)
        assertEquals(Offer.State.INACTIVE, (result as NetworkResult.Success).data.state)
    }

    @Test fun `patchOfferState propage Error`() = runTest {
        // simulate get ok but put fails
        coEvery { offerApi.offersIdGet(any()) } returns Response.success(fakeOfferDto)
        coEvery { offerApi.offersIdPut(any(), any()) } returns
            Response.error(400, "invalid".toResponseBody())

        val request = OfferInput(
            title = "T", description = "D",
            state = OfferInput.State.ACTIVE,
            startDate = today,
            endDate = nextMonth,
            picturePath = "ffdfds"
        )
        assertTrue(dataSource.updateOffer(1, request) is NetworkResult.Error)
    }

    // ── deleteOffer ──────────────────────────────────────────────────────────

    @Test fun `deleteOffer retourne Unit en succes`() = runTest {
        coEvery { offerApi.offersIdDelete(1) } returns Response.success(Unit)

        val result = dataSource.deleteOffer(1)

        assertTrue(result is NetworkResult.Success)
        coVerify(exactly = 1) { offerApi.offersIdDelete(1) }
    }

    @Test fun `deleteOffer propage Error 404`() = runTest {
        coEvery { offerApi.offersIdDelete(1) } returns Response.error(404, "not found".toResponseBody())
        assertTrue(dataSource.deleteOffer(1) is NetworkResult.Error)
    }

    @Test fun `deleteOffer propage Exception`() = runTest {
        coEvery { offerApi.offersIdDelete(any()) } throws RuntimeException("crash")
        assertTrue(dataSource.deleteOffer(1) is NetworkResult.Exception)
    }
}
