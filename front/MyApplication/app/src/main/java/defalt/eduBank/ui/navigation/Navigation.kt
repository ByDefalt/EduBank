package defalt.eduBank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import defalt.featureAccount.ui.navigation.accountGraph
import defalt.featureOffer.ui.navigation.offerGraph
import defalt.ui.utils.Routes

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
