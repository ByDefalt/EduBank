package defalt.domain.entity.bank.entity

import defalt.core.api.bank.model.BankAccountParameterEntity
import defalt.core.api.bank.model.TypeEntity


data class BankAccountDetailsEntity(
    val id: Int,
    val parameterId: Int,
    val typeId: Int,
    val sold: Double,
    val iban: String,
    val parameter: defalt.core.api.bank.model.BankAccountParameterEntity? = null,
    val type: defalt.core.api.bank.model.TypeEntity? = null,

    )
