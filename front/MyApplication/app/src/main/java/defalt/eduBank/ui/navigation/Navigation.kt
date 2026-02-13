package defalt.eduBank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import defalt.core.ui.utils.Routes
import defalt.featureAccount.ui.navigation.accountGraph

@Composable
fun ArkeoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
    ) {
        homeGraph(navController)
        accountGraph(navController)
    }
}
