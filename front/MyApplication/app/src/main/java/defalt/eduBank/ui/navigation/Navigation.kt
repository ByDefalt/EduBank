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
        startDestination = Routes.Core.Home,
    ) {
        homeGraph(
            onNavigateToLogin = { navController.navigate(Routes.Account.Login) },
            onNavigateToRegister = { navController.navigate(Routes.Account.Register) },
            onNavigateToOffer = { navController.navigate(Routes.Offer) },
        )
        accountGraph(
            onBackToHome = { navController.popBackStack() },
            onRegisterSuccess = { navController.navigate(Routes.Core.Home) },
            onLoginSuccess = { navController.navigate(Routes.Bank.Home) },
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
            navController = navController,
            onDismiss = { navController.popBackStack() },
            onVirementClick = { navController.navigate(Routes.Operation.CreateTransfer) },
            onHistoriqueClick = { /* navigate vers historique */ },
            onBeneficiaireClick = { navController.navigate(Routes.Operation.BeneficiaireGraph) },
            onNavigateBack = { navController.popBackStack() },
            onNavigateToHomeBank = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
            onTransferSuccess = { navController.navigate(Routes.Bank.Home) },
            onNavigateToTransferReceiver = { navController.navigate(Routes.Operation.CreateTransfer.Receiver) },
            onNavigateToTransferAmount = { navController.navigate(Routes.Operation.CreateTransfer.Amount) },
            onNavigateToTransferLabel = { navController.navigate(Routes.Operation.CreateTransfer.Label) },
            onNavigateToTransferRecap = { navController.navigate(Routes.Operation.CreateTransfer.Recap) },
            onPopTransferWizard = {
                navController.popBackStack(
                    route = Routes.Operation.CreateTransfer,
                    inclusive = true,
                )
            },
        )
    }
}
