package defalt.featureBank.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.featureBank.ui.screen.HomeAccountScreen
import defalt.featureBank.ui.screen.ListAccountOverviewScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.bankGraph(navController: NavController) {
    navigation<Routes.Account>(
        startDestination = Routes.Account.Login,
    ) {
        composable<Routes.Bank.Home> {
            HomeAccountScreen(
                onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },

            )
        }
        composable<Routes.Bank.ListAccount> {
            ListAccountOverviewScreen(onBack = { navController.popBackStack() })
        }
    }
}
