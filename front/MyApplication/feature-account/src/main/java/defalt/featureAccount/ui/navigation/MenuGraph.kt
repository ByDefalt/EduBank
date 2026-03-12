package defalt.featureAccount.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import defalt.featureAccount.ui.screen.MenuScreen
import defalt.featureAccount.ui.screen.MyAccountDetailsScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.menuGraph(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
) {
    composable<Routes.Core.Menu> {
        MenuScreen(
            onNavigateToProfile = onNavigateToProfile,
            onNavigateToOffers = onNavigateToOffers,
            onLogout = onLogout,
            onBack = onBack,
        )
    }
    composable<Routes.Core.Profile> {
        MyAccountDetailsScreen(
            onBack = onBack,
            onNavigateToHomeBank = onNavigateToHomeBank,
            onNavigateToAccounts = onNavigateToAccounts,
            onNavigateToTransfer = onNavigateToTransfer,
            onNavigateToMenu = onNavigateToMenu,
        )
    }
}
