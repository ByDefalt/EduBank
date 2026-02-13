package defalt.eduBank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import defalt.core.ui.utils.Routes
import defalt.featureAccount.ui.navigation.accountGraph
import defalt.featureOffer.ui.navigation.offerGraph

@Composable
fun ArkeoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Core.Home,
    ) {
        homeGraph(navController)
        accountGraph(navController)
        offerGraph(navController)
    }
}
