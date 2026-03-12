package defalt.network.datasource.offer

import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.network.api.offer.service.OfferApi
import defalt.network.mapper.offer.toDto
import defalt.network.mapper.offer.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class OfferRemoteDataSource(
    private val offerApi: OfferApi,
) : IOfferRemoteDataSource {

    // --- PUBLIC / CLIENT ---

    override suspend fun getActiveOffers(): NetworkResult<List<Offer>> =
        safeApiCall { offerApi.offersActiveGet() }.map { it.toEntity() }

    override suspend fun getOffers(): NetworkResult<List<Offer>> {
        return safeApiCall { offerApi.offersGet() }.map { it.toEntity() }
    }

    override suspend fun getOfferById(id: Int): NetworkResult<Offer> =
        safeApiCall { offerApi.offersIdGet(id) }.map { it.toEntity() }

    // --- ADMIN ---

    override suspend fun createOffer(offerInput: OfferInput): NetworkResult<Offer> =
        safeApiCall { offerApi.offersPost(offerInput.toDto()) }.map { it.toEntity() }

    override suspend fun updateOffer(id: Int, offerInput: OfferInput): NetworkResult<Offer> =
        safeApiCall { offerApi.offersIdPut(id, offerInput.toDto()) }.map { it.toEntity() }

    override suspend fun deleteOffer(id: Int): NetworkResult<Unit> =
        safeApiCall { offerApi.offersIdDelete(id) }
}
