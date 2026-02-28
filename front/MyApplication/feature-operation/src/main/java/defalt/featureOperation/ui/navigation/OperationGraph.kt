package defalt.featureOperation.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import defalt.featureOperation.ui.screen.TransferBottomSheet
import defalt.ui.utils.Routes

fun NavGraphBuilder.operationGraph(
    onDismiss: () -> Unit,
    onVirementClick: () -> Unit = {},
    onHistoriqueClick: () -> Unit = {},
    onBeneficiaireClick: () -> Unit = {},
) {
    navigation<Routes.Operation>(
        startDestination = Routes.Operation.Transfer,
    ) {
        dialog<Routes.Operation.Transfer> {
            TransferBottomSheet(
                onDismiss = onDismiss,
                onVirementClick = onVirementClick,
                onHistoriqueClick = onHistoriqueClick,
                onBeneficiaireClick = onBeneficiaireClick,
            )
        }
    }
}
