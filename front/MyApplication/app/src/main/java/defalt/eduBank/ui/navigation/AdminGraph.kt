package defalt.eduBank.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import defalt.eduBank.ui.screen.AdminHomeScreen
import defalt.featureAccount.ui.screen.AdminAccountDetailScreen
import defalt.featureAccount.ui.screen.AdminAccountListScreen
import defalt.featureBank.ui.screen.AdminBankDetailScreen
import defalt.featureBank.ui.screen.AdminBankListScreen
import defalt.featureOffer.ui.screen.AdminCreateOfferScreen
import defalt.featureOffer.ui.screen.AdminOfferDetailScreen
import defalt.featureOffer.ui.screen.AdminOfferListScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.adminGraph(navController: NavController) {
    // Hub admin (hors sous-graphe pour être accessible depuis Routes.Core.AdminHome)
    composable<Routes.Core.AdminHome> {
        AdminHomeScreen(
            onNavigateToAccounts = { navController.navigate(Routes.Admin.AccountList) },
            onNavigateToBankAccounts = { navController.navigate(Routes.Admin.BankList) },
            onNavigateToOffers = { navController.navigate(Routes.Admin.OfferList) },
        )
    }

    navigation<Routes.Admin>(startDestination = Routes.Admin.AccountList) {
        // ── Comptes utilisateurs ───────────────────────────────────────────
        composable<Routes.Admin.AccountList> {
            AdminAccountListScreen(
                onBack = { navController.navigate(Routes.Core.AdminHome) },
                onItemClick = { id -> navController.navigate(Routes.Admin.AccountDetail(id)) },
            )
        }
        composable<Routes.Admin.AccountDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.AccountDetail>().id
            AdminAccountDetailScreen(
                id = id,
                onBack = { navController.popBackStack() },
            )
        }

        // ── Comptes bancaires ──────────────────────────────────────────────
        composable<Routes.Admin.BankList> {
            AdminBankListScreen(
                onBack = { navController.navigate(Routes.Core.AdminHome) },
                onItemClick = { id -> navController.navigate(Routes.Admin.BankDetail(id)) },
            )
        }
        composable<Routes.Admin.BankDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.BankDetail>().id
            AdminBankDetailScreen(
                id = id,
                onBack = { navController.popBackStack() },
            )
        }

        // ── Offres ─────────────────────────────────────────────────────────
        composable<Routes.Admin.OfferList> {
            AdminOfferListScreen(
                onBack = { navController.navigate(Routes.Core.AdminHome) },
                onItemClick = { id -> navController.navigate(Routes.Admin.OfferDetail(id)) },
                onCreateClick = { navController.navigate(Routes.Admin.CreateOffer) },
            )
        }
        composable<Routes.Admin.OfferDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.OfferDetail>().id
            AdminOfferDetailScreen(
                id = id,
                onBack = { navController.popBackStack() },
            )
        }
        composable<Routes.Admin.CreateOffer> {
            AdminCreateOfferScreen(
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() },
            )
        }
    }
}
