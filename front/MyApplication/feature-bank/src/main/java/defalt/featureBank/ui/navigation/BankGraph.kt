package defalt.featureBank.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import defalt.featureBank.ui.screen.AccountDetailsScreen
import defalt.featureBank.ui.screen.HomeAccountScreen
import defalt.featureBank.ui.screen.ListAccountOverviewScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.bankGraph(
    onNavigateToAccounts: () -> Unit,
    onNavigateToTransfer: () -> Unit,
    onNavigateToHomeBank: () -> Unit,
    onNavigateToAccountDetails: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    navigation<Routes.Bank>(
        startDestination = Routes.Bank.Home,
    ) {
        composable<Routes.Bank.Home> {
            HomeAccountScreen(
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToTransfer = onNavigateToTransfer,
                onNavigateToAccountDetails = onNavigateToAccountDetails,
            )
        }
        composable<Routes.Bank.ListAccount> {
            ListAccountOverviewScreen(
                onNavigateToHomeBank = onNavigateToHomeBank,
                onNavigateToTransfer = onNavigateToTransfer,
                onNavigateToAccountDetails = onNavigateToAccountDetails,
            )
        }
        composable<Routes.Bank.AccountDetails> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Bank.AccountDetails>()
            AccountDetailsScreen(
                accountId = route.accountId,
                onNavigateBack = onNavigateBack,
                onNavigateToHomeBank = onNavigateToHomeBank,
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToTransfer = onNavigateToTransfer,
            )
        }
    }
}
