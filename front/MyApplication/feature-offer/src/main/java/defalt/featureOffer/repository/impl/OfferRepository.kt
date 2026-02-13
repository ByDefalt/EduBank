package defalt.featureOffer.repository.impl

import defalt.core.api.offer.model.Offer
import defalt.core.api.offer.model.OffersIdPutRequest
import defalt.core.api.offer.model.OffersIdStatePatchRequest
import defalt.core.api.offer.model.OffersPostRequest
import defalt.core.api.offer.service.OfferApi
import defalt.core.api.offer.service.OfferApi.StateOffersGet
import defalt.core.utils.NetworkResult
import defalt.core.utils.safeApiCall
import defalt.featureOffer.repository.service.IOfferRepository

class OfferRepository(
    private val api: OfferApi,
) : IOfferRepository {

    override suspend fun getActiveOffers(): NetworkResult<List<Offer>> {
        return safeApiCall { api.offersActiveGet() }
    }

    override suspend fun getOffers(
        state: StateOffersGet?,
        activeOnly: Boolean?,
    ): NetworkResult<List<Offer>> {
        return safeApiCall { api.offersGet(state, activeOnly) }
    }

    override suspend fun getOfferById(id: Int): NetworkResult<Offer> {
        return safeApiCall { api.offersIdGet(id) }
    }

    override suspend fun createOffer(request: OffersPostRequest): NetworkResult<Offer> {
        return safeApiCall { api.offersPost(request) }
    }

    override suspend fun updateOffer(id: Int, request: OffersIdPutRequest): NetworkResult<Offer> {
        return safeApiCall { api.offersIdPut(id, request) }
    }

    override suspend fun changeOfferState(
        id: Int,
        request: OffersIdStatePatchRequest,
    ): NetworkResult<Offer> {
        return safeApiCall { api.offersIdStatePatch(id, request) }
    }

    override suspend fun deleteOffer(id: Int): NetworkResult<Unit> {
        return safeApiCall { api.offersIdDelete(id) }
    }
}
