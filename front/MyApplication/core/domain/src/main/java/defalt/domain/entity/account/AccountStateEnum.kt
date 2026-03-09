package defalt.domain.entity.account

enum class AccountStateEnum(val value: String) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    ENCLOSE("ENCLOSE"),
    ;

    override fun toString(): String = value
}
