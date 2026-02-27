package defalt.core.api.operation.model

data class OperationsIdCancelPost201ResponseEntity(
    val originalOperation: defalt.core.api.operation.model.OperationEntity? = null,
    val cancellationOperation: defalt.core.api.operation.model.OperationEntity? = null,

    )
