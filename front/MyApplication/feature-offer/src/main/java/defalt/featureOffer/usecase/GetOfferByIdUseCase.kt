package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetOfferByIdUseCase(
    private val repository: IOfferRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Offer> {
        logger.debug("GetOfferByIdUseCase")
        return repository.getOfferById(id)
    }
}
