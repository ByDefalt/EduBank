package defalt.domain.entity.bank

/**
 * Domain entity mirroring network BankAccount DTO (summary)
 */
data class BankAccountEntity(
    val id: String? = null,
    val parameterId: Int? = null,
    val typeId: Int? = null,
    val sold: Double? = null,
    val iban: String? = null
)

