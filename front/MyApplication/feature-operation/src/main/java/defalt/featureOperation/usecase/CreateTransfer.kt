package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.domain.repository.service.IOperationRepository
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger
import java.time.OffsetDateTime

class CreateTransfer(
    private val operationRepository: IOperationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        accountSourceId: String,
        ibanTarget: String,
        amount: Double,
        label: String,
    ): NetworkResult<Operation> {
        logger.debug("CreateTransfer")
        val operation = Operation(
            id = 0,
            accountSourceId = accountSourceId,
            label = label,
            state = OperationState.PENDING,
            ibanTarget = ibanTarget,
            amount = amount,
            date = OffsetDateTime.now(),
        )
        return operationRepository.createOperation(operation)
    }
}
