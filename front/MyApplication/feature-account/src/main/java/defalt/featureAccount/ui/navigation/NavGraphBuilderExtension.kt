package defalt.featureAccount.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.core.ui.utils.Routes
import defalt.featureAccount.ui.screen.LoginScreen
import defalt.featureAccount.ui.screen.RegisterScreen

fun NavGraphBuilder.accountGraph(navController: NavController) {
    navigation<Routes.Account>(
        startDestination = Routes.Account.Login,
    ) {
        composable<Routes.Account.Login> {
            LoginScreen(onBackToHome = { navController.popBackStack() })
        }
        composable<Routes.Account.Register> {
            RegisterScreen(onBackToHome = { navController.popBackStack() })
        }
    }
}
