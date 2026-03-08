package defalt.eduBank.ui.navigation

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

fun NavGraphBuilder.adminGraph(
    onNavigateToAccounts: () -> Unit,
    onNavigateToBankAccounts: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToAccountDetail: (String) -> Unit,
    onNavigateToAdminHome: () -> Unit,
    onNavigateToBankDetail: (String) -> Unit,
    onNavigateToOfferDetail: (Int) -> Unit,
    onNavigateToCreateOffer: () -> Unit,
    onBack: () -> Unit,
) {
    // Hub admin (hors sous-graphe pour être accessible depuis Routes.Core.AdminHome)
    composable<Routes.Core.AdminHome> {
        AdminHomeScreen(
            onNavigateToAccounts = onNavigateToAccounts,
            onNavigateToBankAccounts = onNavigateToBankAccounts,
            onNavigateToOffers = onNavigateToOffers,
        )
    }

    navigation<Routes.Admin>(startDestination = Routes.Admin.AccountList) {
        // ── Comptes utilisateurs ───────────────────────────────────────────
        composable<Routes.Admin.AccountList> {
            AdminAccountListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToAccountDetail,
            )
        }
        composable<Routes.Admin.AccountDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.AccountDetail>().id
            AdminAccountDetailScreen(
                id = id,
                onBack = onBack,
            )
        }

        // ── Comptes bancaires ──────────────────────────────────────────────
        composable<Routes.Admin.BankList> {
            AdminBankListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToBankDetail,
            )
        }
        composable<Routes.Admin.BankDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.BankDetail>().id
            AdminBankDetailScreen(
                id = id,
                onBack = onBack,
            )
        }

        // ── Offres ─────────────────────────────────────────────────────────
        composable<Routes.Admin.OfferList> {
            AdminOfferListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToOfferDetail,
                onCreateClick = onNavigateToCreateOffer,
            )
        }
        composable<Routes.Admin.OfferDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.OfferDetail>().id
            AdminOfferDetailScreen(
                id = id,
                onBack = onBack,
            )
        }
        composable<Routes.Admin.CreateOffer> {
            AdminCreateOfferScreen(
                onBack = onBack,
                onSuccess = onBack,
            )
        }
    }
}
