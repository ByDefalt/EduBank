package defalt.featureOffer.usecase

import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class DeleteOfferUseCase(
    private val repository: IOfferRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Unit> {
        logger.debug("DeleteOfferUseCase")
        return repository.deleteOffer(id)
    }
}
