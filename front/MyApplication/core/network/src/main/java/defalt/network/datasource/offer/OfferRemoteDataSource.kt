package defalt.network.datasource.offer

import defalt.domain.datasource.offer.IOfferRemoteDataSource
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.domain.entity.offer.OffersIdStatePatchRequest
import defalt.domain.entity.offer.OffersPostRequest
import defalt.network.api.offer.service.OfferApi
import defalt.network.mapper.offer.toDto
import defalt.network.mapper.offer.toEntity
import defalt.network.utils.safeApiCall
import defalt.utils.NetworkResult
import defalt.utils.map

class OfferRemoteDataSource(
    private val offerApi: OfferApi,
) : IOfferRemoteDataSource {

    // --- PUBLIC ---

    override suspend fun getActiveOffers(): NetworkResult<List<Offer>> =
        safeApiCall { offerApi.offersActiveGet() }
            .map { it.toEntity() }

    // --- ADMIN & CLIENT ---

    override suspend fun getOffers(state: Offer.State?, activeOnly: Boolean?): NetworkResult<List<Offer>> {
        val stateDto = state?.let {
            when (it) {
                Offer.State.ACTIVE -> OfferApi.StateOffersGet.ACTIVE
                Offer.State.INACTIVE -> OfferApi.StateOffersGet.INACTIVE
                Offer.State.EXPIRED -> OfferApi.StateOffersGet.EXPIRED
            }
        }
        return safeApiCall { offerApi.offersGet(stateDto, activeOnly) }
            .map { it.toEntity() }
    }

    override suspend fun getOfferById(id: Int): NetworkResult<Offer> =
        safeApiCall { offerApi.offersIdGet(id) }
            .map { it.toEntity() }

    // --- ADMIN ---

    override suspend fun createOffer(request: OffersPostRequest): NetworkResult<Offer> =
        safeApiCall { offerApi.offersPost(request.toDto()) }
            .map { it.toEntity() }

    override suspend fun updateOffer(id: Int, request: OffersIdPutRequest): NetworkResult<Offer> =
        safeApiCall { offerApi.offersIdPut(id, request.toDto()) }
            .map { it.toEntity() }

    override suspend fun patchOfferState(id: Int, request: OffersIdStatePatchRequest): NetworkResult<Offer> =
        safeApiCall { offerApi.offersIdStatePatch(id, request.toDto()) }
            .map { it.toEntity() }

    override suspend fun deleteOffer(id: Int): NetworkResult<Unit> =
        safeApiCall { offerApi.offersIdDelete(id) }
}
