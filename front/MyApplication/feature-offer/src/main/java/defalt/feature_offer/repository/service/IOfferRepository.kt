package defalt.feature_offer.repository.service

import defalt.core.api.offer.model.Offer
import defalt.core.api.offer.model.OffersIdPutRequest
import defalt.core.api.offer.model.OffersIdStatePatchRequest
import defalt.core.api.offer.model.OffersPostRequest
import defalt.core.api.offer.service.OfferApi.StateOffersGet
import defalt.core.utils.NetworkResult

interface IOfferRepository {

    suspend fun getActiveOffers(): NetworkResult<List<Offer>>

    suspend fun getOffers(
        state: StateOffersGet? = null,
        activeOnly: Boolean? = true
    ): NetworkResult<List<Offer>>

    suspend fun getOfferById(id: Int): NetworkResult<Offer>

    suspend fun createOffer(request: OffersPostRequest): NetworkResult<Offer>

    suspend fun updateOffer(id: Int, request: OffersIdPutRequest): NetworkResult<Offer>

    suspend fun changeOfferState(
        id: Int,
        request: OffersIdStatePatchRequest
    ): NetworkResult<Offer>

    suspend fun deleteOffer(id: Int): NetworkResult<Unit>
}
