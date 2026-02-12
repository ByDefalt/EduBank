package defalt.feature_account.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.feature_account.ui.screen.LoginScreen
import defalt.feature_account.ui.screen.RegisterScreen

fun NavGraphBuilder.accountGraph(navController: NavController) {
    navigation(startDestination = "login", route = "account_root") {
        composable("login") {
            LoginScreen(onBackToHome = { navController.popBackStack() })
        }
        composable("register") {
            RegisterScreen(onBackToHome = { navController.popBackStack() })
        }
    }
}