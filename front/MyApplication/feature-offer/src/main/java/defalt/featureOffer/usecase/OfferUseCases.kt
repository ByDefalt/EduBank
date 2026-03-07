package defalt.featureOffer.usecase

import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.domain.entity.offer.OffersPostRequest
import defalt.domain.repository.service.IOfferRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetAllOffersUseCase(private val repository: IOfferRepository, private val logger: Logger) {
    suspend operator fun invoke(): NetworkResult<List<Offer>> {
        logger.debug("GetAllOffersUseCase")
        return repository.getOffers()
    }
}

class GetOfferByIdUseCase(private val repository: IOfferRepository, private val logger: Logger) {
    suspend operator fun invoke(id: Int): NetworkResult<Offer> {
        logger.debug("GetOfferByIdUseCase")
        return repository.getOfferById(id)
    }
}

class CreateOfferUseCase(private val repository: IOfferRepository, private val logger: Logger) {
    suspend operator fun invoke(request: OffersPostRequest): NetworkResult<Offer> {
        logger.debug("CreateOfferUseCase")
        return repository.createOffer(request)
    }
}

class UpdateOfferUseCase(private val repository: IOfferRepository, private val logger: Logger) {
    suspend operator fun invoke(id: Int, request: OffersIdPutRequest): NetworkResult<Offer> {
        logger.debug("UpdateOfferUseCase")
        return repository.updateOffer(id, request)
    }
}

class DeleteOfferUseCase(private val repository: IOfferRepository, private val logger: Logger) {
    suspend operator fun invoke(id: Int): NetworkResult<Unit> {
        logger.debug("DeleteOfferUseCase")
        return repository.deleteOffer(id)
    }
}
