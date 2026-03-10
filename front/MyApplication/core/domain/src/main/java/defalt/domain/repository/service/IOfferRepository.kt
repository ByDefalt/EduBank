package defalt.domain.repository.service

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.utils.NetworkResult

interface IOfferRepository {

    // --- PUBLIC ---
    /** Récupère uniquement les offres dont l'état est "active" */
    suspend fun getActiveOffers(): NetworkResult<List<Offer>>

    // --- ADMIN & CLIENT ---
    suspend fun getOffers(state: Offer.State? = null, activeOnly: Boolean? = null): NetworkResult<List<Offer>>
    suspend fun getOfferById(id: Int): NetworkResult<Offer>

    // --- ADMIN ---
    suspend fun createOffer(offerInput: OfferInput): NetworkResult<Offer>
    suspend fun updateOffer(id: Int, offerInput: OfferInput): NetworkResult<Offer>
    suspend fun deleteOffer(id: Int): NetworkResult<Unit>
}
