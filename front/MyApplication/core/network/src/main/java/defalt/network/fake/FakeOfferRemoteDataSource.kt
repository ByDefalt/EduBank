package defalt.network.fake

import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.domain.entity.offer.OffersIdStatePatchRequest
import defalt.domain.entity.offer.OffersPostRequest
import defalt.utils.NetworkResult

class FakeOfferRemoteDataSource : IOfferRemoteDataSource {

    private val offers = FakeData.offers.toMutableList()

    // --- PUBLIC ---

    override suspend fun getActiveOffers(): NetworkResult<List<Offer>> =
        NetworkResult.Success(offers.filter { it.state == Offer.State.ACTIVE })

    // --- ADMIN & CLIENT ---

    override suspend fun getOffers(state: Offer.State?, activeOnly: Boolean?): NetworkResult<List<Offer>> {
        var result = offers.toList()
        if (state != null) result = result.filter { it.state == state }
        if (activeOnly == true) result = result.filter { it.state == Offer.State.ACTIVE }
        return NetworkResult.Success(result)
    }

    override suspend fun getOfferById(id: Int): NetworkResult<Offer> =
        offers.find { it.id == id }
            ?.let { NetworkResult.Success(it) }
            ?: NetworkResult.Error(code = 404, message = "Offre introuvable : $id")

    // --- ADMIN ---

    override suspend fun createOffer(request: OffersPostRequest): NetworkResult<Offer> {
        val newOffer = Offer(
            id          = offers.size + 1,
            title       = request.title,
            description = request.description,
            state       = Offer.State.valueOf(request.state.value.uppercase()),
            startDate   = request.startDate,
            endDate     = request.endDate,
            picturePath = request.picturePath,
        )
        offers.add(newOffer)
        return NetworkResult.Success(newOffer)
    }

    override suspend fun updateOffer(id: Int, request: OffersIdPutRequest): NetworkResult<Offer> {
        val index = offers.indexOfFirst { it.id == id }
        return if (index != -1) {
            val current = offers[index]
            val updated = current.copy(
                title       = request.title ?: current.title,
                description = request.description ?: current.description,
                state       = request.state?.let { Offer.State.valueOf(it.value.uppercase()) } ?: current.state,
                startDate   = request.startDate ?: current.startDate,
                endDate     = request.endDate ?: current.endDate,
                picturePath = request.picturePath ?: current.picturePath,
            )
            offers[index] = updated
            NetworkResult.Success(updated)
        } else {
            NetworkResult.Error(code = 404, message = "Offre introuvable : $id")
        }
    }

    override suspend fun patchOfferState(id: Int, request: OffersIdStatePatchRequest): NetworkResult<Offer> {
        val index = offers.indexOfFirst { it.id == id }
        return if (index != -1) {
            val updated = offers[index].copy(
                state = Offer.State.valueOf(request.state.value.uppercase()),
            )
            offers[index] = updated
            NetworkResult.Success(updated)
        } else {
            NetworkResult.Error(code = 404, message = "Offre introuvable : $id")
        }
    }

    override suspend fun deleteOffer(id: Int): NetworkResult<Unit> =
        if (offers.removeIf { it.id == id }) NetworkResult.Success(Unit)
        else NetworkResult.Error(code = 404, message = "Offre introuvable : $id")
}

