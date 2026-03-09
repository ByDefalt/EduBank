package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Operation
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class GetOperationByIdUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Operation> {
        logger.debug("GetOperationByIdUseCase")
        return repository.getOperationById(id)
    }
}
