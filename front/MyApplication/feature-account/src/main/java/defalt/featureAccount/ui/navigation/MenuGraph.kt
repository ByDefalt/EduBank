package defalt.featureAccount.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import defalt.featureAccount.ui.screen.MenuScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.menuGraph(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    composable<Routes.Core.Menu> {
        MenuScreen(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToOffers = onNavigateToOffers,
            onLogout = onLogout,
        )
    }
}
