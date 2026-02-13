package defalt.eduBank.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import defalt.core.ui.utils.Routes
import defalt.core.ui.screen.HomeScreen

fun NavGraphBuilder.homeGraph(navController: NavController) {
    composable<Routes.Home> {
        HomeScreen(
            onNavigateToLogin = { navController.navigate(Routes.Account.Login) },
            onNavigateToRegister = { navController.navigate(Routes.Account.Register) },
        )
    }
}
