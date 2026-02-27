package defalt.core.api.operation.model

data class OperationsIdStatePatchRequestEntity(
    val state: OperationsIdStatePatchRequestEntity.State,

    ) {
    enum class State(val value: String)
}
