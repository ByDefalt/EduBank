package defalt.eduBank.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import defalt.domain.entity.account.RoleEnum
import defalt.featureAccount.ui.navigation.accountGraph
import defalt.featureAccount.ui.navigation.menuGraph
import defalt.featureBank.ui.navigation.bankGraph
import defalt.featureOffer.ui.navigation.offerGraph
import defalt.featureOperation.ui.navigation.operationGraph
import defalt.ui.utils.Routes

@Composable
fun ArkeoNavHost(
    navController: NavHostController,
    startDestination: Routes = Routes.Core.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        homeGraph(
            onNavigateToLogin = { navController.navigate(Routes.Account.Login) },
            onNavigateToRegister = { navController.navigate(Routes.Account.Register) },
            onNavigateToOffer = { navController.navigate(Routes.Offer) },
        )
        accountGraph(
            onBackToHome = { navController.popBackStack() },
            onLoginSuccess = { role ->
                when (role) {
                    RoleEnum.ADMIN -> navController.navigate(Routes.Core.AdminHome) {
                        popUpTo(Routes.Core.Home) { inclusive = false }
                    }
                    RoleEnum.CUSTOMER -> navController.navigate(Routes.Bank.Home) {
                        popUpTo(Routes.Core.Home) { inclusive = false }
                    }
                }
            },
        )
        offerGraph(
            onBack = { navController.popBackStack() },
            onNavigateToHome = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
            onNavigateToMenu = { navController.navigate(Routes.Core.Menu) },
        )
        bankGraph(
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
            onNavigateToHomeBank = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccountDetails = { accountId -> navController.navigate(Routes.Bank.AccountDetails(accountId)) },
            onNavigateBack = { navController.popBackStack() },
            onNavigateToMenu = { navController.navigate(Routes.Core.Menu) },
        )
        operationGraph(
            navController = navController,
            onDismiss = { navController.popBackStack() },
            onVirementClick = { navController.navigate(Routes.Operation.CreateTransfer) },
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
                navController.popBackStack(route = Routes.Operation.CreateTransfer, inclusive = true)
            },
        )
        // ── Admin ──────────────────────────────────────────────────────────
        adminGraph(
            onNavigateToAccounts = { navController.navigate(Routes.Admin.AccountList) },
            onNavigateToBankAccounts = { navController.navigate(Routes.Admin.BankList) },
            onNavigateToOffers = { navController.navigate(Routes.Admin.OfferList) },
            onNavigateToOperations = { navController.navigate(Routes.Admin.OperationList) },
            onNavigateToAccountDetail = { id -> navController.navigate(Routes.Admin.AccountDetail(id)) },
            onNavigateToAdminHome = { navController.navigate(Routes.Core.AdminHome) },
            onNavigateToBankDetail = { id -> navController.navigate(Routes.Admin.BankDetail(id)) },
            onNavigateToCreateBankAccount = { navController.navigate(Routes.Admin.CreateBankAccount) },
            onNavigateToOfferDetail = { id -> navController.navigate(Routes.Admin.OfferDetail(id)) },
            onNavigateToCreateOffer = { navController.navigate(Routes.Admin.CreateOffer) },
            onNavigateToOperationDetail = { id ->
                navController.navigate(
                    Routes.Admin.OperationDetail(
                        id,
                    ),
                )
            },
            onBack = { navController.popBackStack() },
            onLogout = {
                navController.navigate(Routes.Core.Home) {
                    popUpTo(0) { inclusive = true }
                }
            },
            navController = navController,
        )
        // ── Menu ───────────────────────────────────────────────────────────
        menuGraph(
            onNavigateToProfile = { navController.navigate(Routes.Core.Profile) },
            onNavigateToOffers = { navController.navigate(Routes.Offer.List) },
            onLogout = {
                navController.navigate(Routes.Core.Home) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() },
            onNavigateToHomeBank = { navController.navigate(Routes.Bank.Home) },
            onNavigateToAccounts = { navController.navigate(Routes.Bank.ListAccount) },
            onNavigateToTransfer = { navController.navigate(Routes.Operation) },
            onNavigateToMenu = { navController.navigate(Routes.Core.Menu) },
        )
    }
}
