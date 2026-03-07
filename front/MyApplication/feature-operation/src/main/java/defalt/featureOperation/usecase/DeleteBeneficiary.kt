package defalt.featureOperation.usecase

import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class DeleteBeneficiary(
    private val operationRepository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Unit> {
        logger.debug("DeleteBeneficiary")
        return operationRepository.deleteBeneficiary(id)
    }
}
