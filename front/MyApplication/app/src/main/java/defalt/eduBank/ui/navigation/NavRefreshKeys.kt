package defalt.eduBank.ui.navigation

sealed class NavRefreshKey {
    data object Bank : NavRefreshKey()
    data object Account : NavRefreshKey()
    data object Offer : NavRefreshKey()
    data object Operation : NavRefreshKey()

    override fun toString() = this::class.simpleName!!
}