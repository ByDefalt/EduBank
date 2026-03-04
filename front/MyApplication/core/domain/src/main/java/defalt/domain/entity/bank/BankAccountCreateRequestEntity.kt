package defalt.domain.entity.bank

/**
 * Domain entity for creating bank account
 */
data class BankAccountCreateRequestEntity(
    val parameterId: Int,
    val typeId: Int,
    val iban: String
)

