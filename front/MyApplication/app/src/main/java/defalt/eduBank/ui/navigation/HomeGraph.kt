package defalt.eduBank.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import defalt.ui.screen.HomeScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.homeGraph(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToOffer: () -> Unit,
) {
    composable<Routes.Core.Home> {
        HomeScreen(
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToRegister = onNavigateToRegister,
            onNavigateToOffer = onNavigateToOffer,
        )
    }
}
