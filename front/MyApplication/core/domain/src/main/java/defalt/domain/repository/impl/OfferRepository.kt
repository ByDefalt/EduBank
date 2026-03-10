package defalt.domain.repository.impl

import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult

class OfferRepository(
    private val remoteDataSource: IOfferRemoteDataSource,
) : IOfferRepository {

    // --- PUBLIC ---

    override suspend fun getActiveOffers(): NetworkResult<List<Offer>> =
        remoteDataSource.getActiveOffers()

    // --- ADMIN & CLIENT ---

    override suspend fun getOffers(state: Offer.State?, activeOnly: Boolean?): NetworkResult<List<Offer>> =
        remoteDataSource.getOffers(state, activeOnly)

    override suspend fun getOfferById(id: Int): NetworkResult<Offer> =
        remoteDataSource.getOfferById(id)

    // --- ADMIN ---

    override suspend fun createOffer(offerInput: OfferInput): NetworkResult<Offer> =
        remoteDataSource.createOffer(offerInput)

    override suspend fun updateOffer(id: Int, offerInput: OfferInput): NetworkResult<Offer> =
        remoteDataSource.updateOffer(id, offerInput)

    override suspend fun deleteOffer(id: Int): NetworkResult<Unit> =
        remoteDataSource.deleteOffer(id)
}