package defalt.core.api.operation.model

data class OperationsIdCancelPost201ResponseEntity(
    val originalOperation: Operation? = null,
    val cancellationOperation: Operation? = null,

)
