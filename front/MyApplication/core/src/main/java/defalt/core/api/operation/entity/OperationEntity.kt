package defalt.core.api.operation.model

data class OperationEntity(
    val id: Int,
    val bankAccountSourceId: Int,
    val label: String,
    val state: Operation.State,
    val ibanTarget: String,
    val amount: Double,
    val date: java.time.OffsetDateTime,
    val createdAt: java.time.OffsetDateTime? = null,
    val updatedAt: java.time.OffsetDateTime? = null,

) {
    enum class State(val value: String)
}
