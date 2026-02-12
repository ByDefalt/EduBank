package defalt.eduBank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import defalt.core.ui.Routes
import defalt.core.ui.screen.HomeScreen
import defalt.feature_account.ui.navigation.accountGraph


@Composable
fun ArkeoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen(
                onNavigateToLogin = { navController.navigate(Routes.Account.Login) },
                onNavigateToRegister = { navController.navigate(Routes.Account.Register) }
            )
        }
        accountGraph(navController)
    }
}