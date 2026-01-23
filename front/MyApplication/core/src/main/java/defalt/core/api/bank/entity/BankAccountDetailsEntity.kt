package defalt.core.api.bank.model

import defalt.core.api.bank.model.BankAccountParameter
import defalt.core.api.bank.model.Type

data class BankAccountDetailsEntity (
    val id: Int,
    val parameterId: Int,
    val typeId: Int,
    val sold: Double,
    val iban: String,
    val parameter: BankAccountParameter? = null,
    val type: Type? = null

) {

}
