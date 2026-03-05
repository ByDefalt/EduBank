package defalt.domain.repository.service

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.domain.entity.offer.OffersIdStatePatchRequest
import defalt.domain.entity.offer.OffersPostRequest
import defalt.utils.NetworkResult

interface IOfferRepository {
    // --- PUBLIC ---
    suspend fun getActiveOffers(): NetworkResult<List<Offer>>

    // --- ADMIN & CLIENT ---
    suspend fun getOffers(state: Offer.State? = null, activeOnly: Boolean? = null): NetworkResult<List<Offer>>
    suspend fun getOfferById(id: Int): NetworkResult<Offer>

    // --- ADMIN ---
    suspend fun createOffer(request: OffersPostRequest): NetworkResult<Offer>
    suspend fun updateOffer(id: Int, request: OffersIdPutRequest): NetworkResult<Offer>
    suspend fun patchOfferState(id: Int, request: OffersIdStatePatchRequest): NetworkResult<Offer>
    suspend fun deleteOffer(id: Int): NetworkResult<Unit>
}
