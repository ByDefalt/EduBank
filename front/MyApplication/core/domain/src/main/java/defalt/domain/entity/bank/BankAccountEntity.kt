package defalt.core.api.bank.model

data class BankAccountEntity(
    val id: Int,
    val parameterId: Int,
    val typeId: Int,
    val sold: Double,
    val iban: String,

)
