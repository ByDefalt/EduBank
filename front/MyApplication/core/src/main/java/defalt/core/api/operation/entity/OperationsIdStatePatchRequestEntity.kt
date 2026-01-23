package defalt.core.api.operation.model

data class OperationsIdStatePatchRequestEntity (
    val state: OperationsIdStatePatchRequest.State

) {
    enum class State(val value: String) {
    }

}
