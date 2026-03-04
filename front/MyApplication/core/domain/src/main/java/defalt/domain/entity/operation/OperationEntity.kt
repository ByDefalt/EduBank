package defalt.domain.entity.operation

import java.time.OffsetDateTime

/**
 * Domain entity mirroring network Operation DTO
 */
data class OperationEntity(
    val id: Int,
    val accountSourceId: String,
    val label: String,
    val state: OperationState,
    val ibanTarget: String,
    val amount: Double,
    val date: OffsetDateTime
)

enum class OperationState(val value: String) {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled")
}
