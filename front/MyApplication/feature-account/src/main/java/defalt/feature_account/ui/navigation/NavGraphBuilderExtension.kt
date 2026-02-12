package defalt.feature_account.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.core.ui.Routes
import defalt.feature_account.ui.screen.LoginScreen
import defalt.feature_account.ui.screen.RegisterScreen

fun NavGraphBuilder.accountGraph(navController: NavController) {
    navigation<Routes.AccountGraph>(
        startDestination = Routes.Login
    ) {
        composable<Routes.Login> {
            LoginScreen(onBackToHome = { navController.popBackStack() })
        }
        composable<Routes.Register> {
            RegisterScreen(onBackToHome = { navController.popBackStack() })
        }
    }
}