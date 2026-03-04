package defalt.domain.entity.operation

import defalt.core.api.operation.model.OperationEntity

data class OperationsGet200ResponseEntity(
    val `data`: List<OperationEntity>? = null,

    )
