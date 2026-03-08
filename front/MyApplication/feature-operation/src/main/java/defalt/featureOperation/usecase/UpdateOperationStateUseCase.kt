package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Operation
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger

class UpdateOperationStateUseCase(
    private val repository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(id: Int, state: String): NetworkResult<Operation> {
        logger.debug("UpdateOperationStateUseCase")
        return repository.updateOperationState(id, state)
    }
}
