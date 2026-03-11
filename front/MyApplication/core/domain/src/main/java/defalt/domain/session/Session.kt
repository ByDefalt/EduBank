package defalt.domain.session

import defalt.domain.entity.account.RoleEnum

data class Session(
    var token: String? = null,
    var accountId: String? = null,
    var role: RoleEnum? = null,
)
