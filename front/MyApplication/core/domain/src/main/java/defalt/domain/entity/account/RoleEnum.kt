package defalt.domain.entity.account

enum class RoleEnum(val value: String) {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    ;

    override fun toString(): String = value
}
