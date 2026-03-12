package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetAllOffersActiveUseCase(
    private val repository: IOfferRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Offer>> {
        logger.debug("GetAllOffersUseCase")
        return repository.getActiveOffers()
    }
}
