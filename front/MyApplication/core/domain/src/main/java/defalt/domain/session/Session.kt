package defalt.domain.session

data class Session(
    var token: String? = null,
    var accountId: String? = null,
    var role: String? = null,
)
