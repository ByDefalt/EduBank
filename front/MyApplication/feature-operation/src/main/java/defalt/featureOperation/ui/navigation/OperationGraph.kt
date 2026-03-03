package defalt.featureOperation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import defalt.featureOperation.ui.screen.BeneficiariesScreen
import defalt.featureOperation.ui.screen.TransferBottomSheet
import defalt.ui.utils.Routes

fun NavGraphBuilder.operationGraph(
    onDismiss: () -> Unit,
    onVirementClick: () -> Unit = {},
    onHistoriqueClick: () -> Unit = {},
    onBeneficiaireClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    navigation<Routes.Operation>(
        startDestination = Routes.Operation.BottomSheet,
    ) {
        dialog<Routes.Operation.BottomSheet> {
            TransferBottomSheet(
                onDismiss = onDismiss,
                onVirementClick = onVirementClick,
                onHistoriqueClick = onHistoriqueClick,
                onBeneficiaireClick = onBeneficiaireClick,
            )
        }

        composable<Routes.Operation.Beneficiaire> {
            BeneficiariesScreen(
                onBack = onNavigateBack,
                onNavigateToHomeBank = onNavigateToHomeBank,
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToTransfer = onNavigateToTransfer,
            )

        }
    }
}
