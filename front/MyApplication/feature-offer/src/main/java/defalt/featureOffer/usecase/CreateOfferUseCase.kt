package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersPostRequest
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class CreateOfferUseCase(
    private val repository: IOfferRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(request: OffersPostRequest): NetworkResult<Offer> {
        logger.debug("CreateOfferUseCase")
        return repository.createOffer(request)
    }
}
