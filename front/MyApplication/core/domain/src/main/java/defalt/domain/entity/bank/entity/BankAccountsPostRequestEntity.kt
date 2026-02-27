package defalt.core.api.bank.model

data class BankAccountsPostRequestEntity(
    val parameterId: Int,
    val typeId: Int,
    val sold: Double,
    val iban: String,
    val accountId: Int,

)
