package defalt.featureOperation.usecase

import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.domain.repository.service.IOperationRepository
import defalt.domain.session.Session
import defalt.utils.NetworkResult
import defalt.utils.logger.Logger
import java.time.OffsetDateTime

class CreateTransfer(
    private val operationRepository: IOperationRepository,
    private val session: Session,
    private val logger: Logger,
) {
    suspend operator fun invoke(
        ibanTarget: String,
        amount: Double,
        label: String,
    ): NetworkResult<Operation> {
        logger.debug("CreateTransfer")
        val operation = Operation(
            id = 0,
            accountSourceId = session.accountId!!,
            label = label,
            state = OperationState.PENDING,
            ibanTarget = ibanTarget,
            amount = amount,
            date = OffsetDateTime.now(),
        )
        return operationRepository.createOperation(operation)
    }
}
