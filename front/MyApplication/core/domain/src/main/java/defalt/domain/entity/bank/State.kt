package defalt.domain.entity.bank

enum class State(val value: String) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOQUED("BLOQUED"),
    CLOSED("CLOSED"),
    ;

    override fun toString(): String = value
}
