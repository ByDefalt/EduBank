package defalt.eduBank.ui.navigation

sealed class NavRefreshKeys {
    data object Bank : NavRefreshKeys()
    data object Account : NavRefreshKeys()
    data object Offer : NavRefreshKeys()
    data object Operation : NavRefreshKeys()

    override fun toString() = this::class.simpleName!!
}
