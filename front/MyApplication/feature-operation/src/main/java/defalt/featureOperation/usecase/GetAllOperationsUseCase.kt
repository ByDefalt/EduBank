package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Operation
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetAllOperationsUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(): NetworkResult<List<Operation>> {
        logger.debug("GetAllOperationsUseCase")
        return repository.getOperations()
    }
}
