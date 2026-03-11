package defalt.domain.entity.operation

enum class OperationState(val value: String) {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),
    CANCELLED("CANCELLED"),
    ;

    override fun toString(): String = value
}
