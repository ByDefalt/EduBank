package defalt.domain.entity.bank

enum class State(val value: String) {
    ACTIVE("active"),
    INACTIVE("inactive"),
    BLOQUED("bloqued"),
    CLOSED("closed"),
    ;

    override fun toString(): String = value
}
