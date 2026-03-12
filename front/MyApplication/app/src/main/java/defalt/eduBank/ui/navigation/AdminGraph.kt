package defalt.eduBank.ui.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import defalt.featureAccount.ui.screen.AdminAccountDetailScreen
import defalt.featureAccount.ui.screen.AdminAccountListScreen
import defalt.featureAccount.ui.screen.AdminHomeScreen
import defalt.featureBank.ui.screen.AdminBankDetailScreen
import defalt.featureBank.ui.screen.AdminBankListScreen
import defalt.featureBank.ui.screen.AdminCreateBankAccountScreen
import defalt.featureOffer.ui.screen.AdminCreateOfferScreen
import defalt.featureOffer.ui.screen.AdminOfferDetailScreen
import defalt.featureOffer.ui.screen.AdminOfferListScreen
import defalt.featureOperation.ui.screen.AdminOperationDetailScreen
import defalt.featureOperation.ui.screen.AdminOperationListScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.adminGraph(
    navController: NavController,
    onNavigateToAccounts: () -> Unit,
    onNavigateToBankAccounts: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToOperations: () -> Unit,
    onNavigateToAccountDetail: (String) -> Unit,
    onNavigateToAdminHome: () -> Unit,
    onNavigateToBankDetail: (String) -> Unit,
    onNavigateToCreateBankAccount: () -> Unit,
    onNavigateToOfferDetail: (Int) -> Unit,
    onNavigateToCreateOffer: () -> Unit,
    onNavigateToOperationDetail: (Int) -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    composable<Routes.Core.AdminHome> {
        AdminHomeScreen(
            onNavigateToAccounts = onNavigateToAccounts,
            onNavigateToBankAccounts = onNavigateToBankAccounts,
            onNavigateToOffers = onNavigateToOffers,
            onNavigateToOperations = onNavigateToOperations,
            onLogout = onLogout,
        )
    }

    navigation<Routes.Admin>(startDestination = Routes.Admin.AccountList) {
        composable<Routes.Admin.AccountList> { entry ->
            val shouldRefresh = entry.savedStateHandle
                .getStateFlow(NavRefreshKeys.Account.toString(), false)
                .collectAsStateWithLifecycle()

            AdminAccountListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToAccountDetail,
                onRefreshConsumed = { entry.savedStateHandle[NavRefreshKeys.Account.toString()] = false },
                shouldRefresh = shouldRefresh.value,
            )
        }
        composable<Routes.Admin.AccountDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.AccountDetail>().id
            val previousEntry = navController.previousBackStackEntry
            AdminAccountDetailScreen(
                id = id,
                onBack = onBack,
                onMutationSuccess = {
                    previousEntry?.savedStateHandle?.set(NavRefreshKeys.Account.toString(), true)
                },
            )
        }

        composable<Routes.Admin.BankList> { entry ->
            val shouldRefresh = entry.savedStateHandle
                .getStateFlow(NavRefreshKeys.Bank.toString(), false)
                .collectAsStateWithLifecycle()

            AdminBankListScreen(
                shouldRefresh = shouldRefresh.value,
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToBankDetail,
                onCreateClick = onNavigateToCreateBankAccount,
                onRefreshConsumed = { entry.savedStateHandle[NavRefreshKeys.Bank.toString()] = false },
            )
        }
        composable<Routes.Admin.BankDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.BankDetail>().id
            val previousEntry = navController.previousBackStackEntry

            AdminBankDetailScreen(
                id = id,
                onBack = onBack,
                onMutationSuccess = {
                    previousEntry?.savedStateHandle?.set(NavRefreshKeys.Bank.toString(), true)
                },
            )
        }
        composable<Routes.Admin.CreateBankAccount> {
            AdminCreateBankAccountScreen(
                onBack = onBack,
                onSuccess = onBack,
            )
        }

        composable<Routes.Admin.OfferList> { entry ->
            val shouldRefresh = entry.savedStateHandle
                .getStateFlow(NavRefreshKeys.Offer.toString(), false)
                .collectAsStateWithLifecycle()

            AdminOfferListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToOfferDetail,
                onCreateClick = onNavigateToCreateOffer,
                onRefreshConsumed = { entry.savedStateHandle[NavRefreshKeys.Offer.toString()] = false },
                shouldRefresh = shouldRefresh.value,
            )
        }
        composable<Routes.Admin.OfferDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.OfferDetail>().id
            val previousEntry = navController.previousBackStackEntry
            AdminOfferDetailScreen(
                id = id,
                onBack = onBack,
                onMutationSuccess = {
                    previousEntry?.savedStateHandle?.set(NavRefreshKeys.Offer.toString(), true)
                },
            )
        }
        composable<Routes.Admin.CreateOffer> {
            AdminCreateOfferScreen(
                onBack = onBack,
                onSuccess = onBack,
            )
        }

        composable<Routes.Admin.OperationList> { entry ->
            val shouldRefresh = entry.savedStateHandle
                .getStateFlow(NavRefreshKeys.Operation.toString(), false)
                .collectAsStateWithLifecycle()

            AdminOperationListScreen(
                onBack = onNavigateToAdminHome,
                onItemClick = onNavigateToOperationDetail,
                onRefreshConsumed = { entry.savedStateHandle[NavRefreshKeys.Operation.toString()] = false },
                shouldRefresh = shouldRefresh.value,
            )
        }
        composable<Routes.Admin.OperationDetail> { entry ->
            val id = entry.toRoute<Routes.Admin.OperationDetail>().id
            val previousEntry = navController.previousBackStackEntry
            AdminOperationDetailScreen(
                id = id,
                onBack = onBack,
                onMutationSuccess = {
                    previousEntry?.savedStateHandle?.set(NavRefreshKeys.Operation.toString(), true)
                },
            )
        }
    }
}
