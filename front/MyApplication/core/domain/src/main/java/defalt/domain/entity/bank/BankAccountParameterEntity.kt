package defalt.domain.entity.bank

/**
 * Domain entity mirroring network BankAccountParameter DTO
 */
data class BankAccountParameterEntity(
    val id: Int? = null,
    val overdraftLimit: Double? = null,
    val state: BankState? = null
)

enum class BankState(val value: String) {
    ACTIVE("active"),
    INACTIVE("inactive"),
    BLOQUED("bloqued"),
    CLOSED("closed");
    override fun toString(): String = value
}

