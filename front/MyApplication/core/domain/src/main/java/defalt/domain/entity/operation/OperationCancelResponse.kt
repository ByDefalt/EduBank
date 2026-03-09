package defalt.domain.entity.operation

data class OperationCancelResponse(
    val originalOperation: Operation? = null,
    val cancellationOperation: Operation? = null,
)
