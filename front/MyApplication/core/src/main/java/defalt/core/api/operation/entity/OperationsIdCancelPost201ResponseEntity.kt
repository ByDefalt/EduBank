package defalt.core.api.operation.model

import defalt.core.api.operation.model.Operation

data class OperationsIdCancelPost201ResponseEntity (
    val originalOperation: Operation? = null,
    val cancellationOperation: Operation? = null

) {

}
