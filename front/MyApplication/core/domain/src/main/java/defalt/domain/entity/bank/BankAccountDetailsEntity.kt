package defalt.domain.entity.bank

import defalt.core.api.bank.model.BankAccountParameterEntity
import defalt.core.api.bank.model.TypeEntity

data class BankAccountDetailsEntity(
    val id: Int,
    val parameterId: Int,
    val typeId: Int,
    val sold: Double,
    val iban: String,
    val parameter: BankAccountParameterEntity? = null,
    val type: TypeEntity? = null,

)
