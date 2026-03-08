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

class GetOperationByIdUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Operation> {
        logger.debug("GetOperationByIdUseCase")
        return repository.getOperationById(id)
    }
}

class CancelOperationUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int): NetworkResult<Operation> {
        logger.debug("CancelOperationUseCase")
        return repository.cancelOperation(id)
    }
}

class UpdateOperationStateUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int, state: String): NetworkResult<Operation> {
        logger.debug("UpdateOperationStateUseCase")
        return repository.updateOperationState(id, state)
    }
}
