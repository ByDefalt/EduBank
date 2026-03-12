package defalt.domain.repository.service

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.utils.NetworkResult

interface IOfferRepository {


    /** Récupère uniquement les offres dont l'état est "active" */
    suspend fun getActiveOffers(): NetworkResult<List<Offer>>


    suspend fun getOffers(): NetworkResult<List<Offer>>
    suspend fun getOfferById(id: Int): NetworkResult<Offer>


    suspend fun createOffer(offerInput: OfferInput): NetworkResult<Offer>
    suspend fun updateOffer(id: Int, offerInput: OfferInput): NetworkResult<Offer>
    suspend fun deleteOffer(id: Int): NetworkResult<Unit>
}
