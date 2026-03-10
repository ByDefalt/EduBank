package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OfferInput
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class UpdateOfferUseCase(
    private val repository: IOfferRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int, request: OfferInput): NetworkResult<Offer> {
        logger.debug("UpdateOfferUseCase")
        return repository.updateOffer(id, request)
    }
}
