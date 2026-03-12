package defalt.featureAccount.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.featureAccount.usecase.AccountWithInfo
import defalt.featureAccount.viewModel.MyAccountDetailsViewModel
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoLabelValue
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.BottomNavBar
import defalt.ui.component.UiStateHandler
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import org.koin.androidx.compose.koinViewModel


@Composable
fun MyAccountDetailsScreen(
    onBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
    viewModel: MyAccountDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.load() }

    MyAccountDetailsContent(
        uiState = uiState,
        onRetry = viewModel::load,
        onBack = onBack,
        onNavigateToHomeBank = onNavigateToHomeBank,
        onNavigateToAccounts = onNavigateToAccounts,
        onNavigateToTransfer = onNavigateToTransfer,
        onNavigateToMenu = onNavigateToMenu,
    )
}


@Composable
internal fun MyAccountDetailsContent(
    uiState: UiState<AccountWithInfo>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
) {
    val safeBack = safeClick(onBack)
    val safeNavigateHome = safeClick(onNavigateToHomeBank)
    val safeNavigateAccounts = safeClick(onNavigateToAccounts)
    val safeNavigateTransfer = safeClick(onNavigateToTransfer)
    val safeNavigateMenu = safeClick(onNavigateToMenu)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArkeoTopBar(title = "MON COMPTE", onBack = safeBack)

            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = CustomColor.ArkeoRed,
                errorColor = CustomColor.ArkeoRed,
            ) { data: AccountWithInfo ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    InfoCard(data)
                }
            }

            BottomNavBar(
                selectedRoute = Routes.Core.Menu,
                mapItems = mapOf(
                    Routes.Bank.Home to safeNavigateHome,
                    Routes.Bank.ListAccount to safeNavigateAccounts,
                    Routes.Operation to safeNavigateTransfer,
                    Routes.Core.Menu to safeNavigateMenu,
                ),
            )
        }
    }
}


@Composable
private fun InfoCard(data: AccountWithInfo) {
    ArkeoCard(title = "INFORMATIONS") {
        ArkeoLabelValue("ID", data.account.id ?: "-")
        ArkeoLabelValue("État", data.account.state?.value ?: "-")
        ArkeoLabelValue("Prénom", data.personalInfo.firstname ?: "-")
        ArkeoLabelValue("Nom", data.personalInfo.lastname ?: "-")
        ArkeoLabelValue("Email", data.personalInfo.email ?: "-")
        ArkeoLabelValue("Téléphone", data.personalInfo.phoneNumber ?: "-")
        ArkeoLabelValue("Adresse", data.personalInfo.address ?: "-")
    }
}


@Preview(showBackground = true, name = "State - Loading")
@Composable
private fun PreviewLoading() {
    MyAccountDetailsContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
private fun PreviewError() {
    MyAccountDetailsContent(uiState = UiState.Error("Impossible de charger le compte"))
}
