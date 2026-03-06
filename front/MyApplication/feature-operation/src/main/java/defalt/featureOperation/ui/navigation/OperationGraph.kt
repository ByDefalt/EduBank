package defalt.featureOperation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import defalt.featureOperation.ui.screen.BeneficiariesScreen
import defalt.featureOperation.ui.screen.TransferBottomSheet
import defalt.featureOperation.ui.screen.transfer.CreateTransferAmountScreen
import defalt.featureOperation.ui.screen.transfer.CreateTransferDebitScreen
import defalt.featureOperation.ui.screen.transfer.CreateTransferLabelScreen
import defalt.featureOperation.ui.screen.transfer.CreateTransferRecapScreen
import defalt.featureOperation.ui.screen.transfer.CreateTransferReceiverScreen
import defalt.featureOperation.viewModel.CreateTransferViewModel
import defalt.ui.utils.Routes
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.operationGraph(
    navController: NavController,
    onDismiss: () -> Unit,
    onVirementClick: () -> Unit = {},
    onHistoriqueClick: () -> Unit = {},
    onBeneficiaireClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onTransferSuccess: () -> Unit = {},
    onNavigateToTransferReceiver: () -> Unit = {},
    onNavigateToTransferAmount: () -> Unit = {},
    onNavigateToTransferLabel: () -> Unit = {},
    onNavigateToTransferRecap: () -> Unit = {},
    onPopTransferWizard: () -> Unit = {},
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

        navigation<Routes.Operation.CreateTransfer>(
            startDestination = Routes.Operation.CreateTransfer.Debit,
        ) {
            composable<Routes.Operation.CreateTransfer.Debit> { entry ->
                val vm = transferViewModel(navController, entry)
                CreateTransferDebitScreen(viewModel = vm, onNext = onNavigateToTransferReceiver)
            }
            composable<Routes.Operation.CreateTransfer.Receiver> { entry ->
                val vm = transferViewModel(navController, entry)
                CreateTransferReceiverScreen(viewModel = vm, onBack = onNavigateBack, onNext = onNavigateToTransferAmount)
            }
            composable<Routes.Operation.CreateTransfer.Amount> { entry ->
                val vm = transferViewModel(navController, entry)
                CreateTransferAmountScreen(viewModel = vm, onBack = onNavigateBack, onNext = onNavigateToTransferLabel)
            }
            composable<Routes.Operation.CreateTransfer.Label> { entry ->
                val vm = transferViewModel(navController, entry)
                CreateTransferLabelScreen(viewModel = vm, onBack = onNavigateBack, onNext = onNavigateToTransferRecap)
            }
            composable<Routes.Operation.CreateTransfer.Recap> { entry ->
                val vm = transferViewModel(navController, entry)
                CreateTransferRecapScreen(
                    viewModel = vm,
                    onBack = onNavigateBack,
                    onSuccess = {
                        onPopTransferWizard()
                        onTransferSuccess()
                    },
                )
            }
        }
    }
}

@Composable
private fun transferViewModel(
    navController: NavController,
    entry: NavBackStackEntry,
): CreateTransferViewModel {
    val parentEntry = remember(entry) {
        navController.getBackStackEntry<Routes.Operation.CreateTransfer>()
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}
