package defalt.eduBank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import defalt.featureAccount.ui.navigation.accountGraph
import defalt.featureBank.ui.navigation.bankGraph
import defalt.featureOffer.ui.navigation.offerGraph
import defalt.featureOperation.ui.navigation.operationGraph
import defalt.ui.utils.Routes

@Composable
fun ArkeoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Bank,
    ) {
        homeGraph(
            onNavigateToLogin = { navController.navigate(Routes.Account.Login) },
            onNavigateToRegister = { navController.navigate(Routes.Account.Register) },
            onNavigateToOffer = { navController.navigate(Routes.Offer) },
        )
        accountGraph(
            onBackToHome = { navController.popBackStack() },
        )
        offerGraph(
            onBack = { navController.popBackStack() },
        )
        bankGraph(
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
            onNavigateToHomeBank = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccountDetails = { accountId -> navController.navigate(Routes.Bank.AccountDetails(accountId)) },
            onNavigateBack = { navController.popBackStack() },
        )
        operationGraph(
            onDismiss = { navController.popBackStack() },
            onVirementClick = { /* navigate vers écran virement */ },
            onHistoriqueClick = { /* navigate vers historique */ },
            onBeneficiaireClick = {
                navController.navigate(Routes.Operation.Beneficiaire)
            },
            onNavigateBack = { navController.popBackStack() },
            onNavigateToHomeBank = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
        )
    }
}
