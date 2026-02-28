package defalt.featureBank.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.featureBank.ui.screen.HomeAccountScreen
import defalt.featureBank.ui.screen.ListAccountOverviewScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.bankGraph(
    onNavigateToAccounts: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToHomeBank: () -> Unit,
) {
    navigation<Routes.Bank>(
        startDestination = Routes.Bank.Home,
    ) {
        composable<Routes.Bank.Home> {
            HomeAccountScreen(
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToTransfer = onNavigateToTransfer,
            )
        }
        composable<Routes.Bank.ListAccount> {
            ListAccountOverviewScreen(
                onNavigateToHomeBank = onNavigateToHomeBank,
                onNavigateToTransfer = onNavigateToTransfer,
            )
        }
    }
}
