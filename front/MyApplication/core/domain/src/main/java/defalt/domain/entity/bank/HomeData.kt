package defalt.domain.entity.bank

import defalt.domain.entity.account.PersonalInformation

data class HomeData(
    val account: BankAccountDetail,
    val personalInformation: PersonalInformation,
)

