package defalt.domain.entity.operation

import java.time.OffsetDateTime

data class Operation(
    val id: Int,
    val accountSourceId: String,
    val label: String,
    val state: OperationState,
    val ibanTarget: String,
    val amount: Double,
    val date: OffsetDateTime,
)
