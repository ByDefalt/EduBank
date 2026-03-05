package defalt.domain.entity.operation

enum class OperationState(val value: String) {
    PENDING("pending"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled"),
    ;

    override fun toString(): String = value
}
