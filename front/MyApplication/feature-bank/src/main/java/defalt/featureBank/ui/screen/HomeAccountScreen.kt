package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.account.PersonalInformation
import defalt.domain.entity.bank.BankAccountDetail
import defalt.domain.entity.bank.BankAccountParameter
import defalt.domain.entity.bank.Type
import defalt.featureBank.viewModel.HomeAccountViewModel
import defalt.featureBank.viewModel.HomeData
import defalt.ui.component.ArkeoQuickAction
import defalt.ui.component.BottomNavBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

private val ArkeoRed = CustomColor.ArkeoRed
private val LightGray = CustomColor.BackgroundGray
private val TextPrimary = CustomColor.TextPrimary
private val TextSecondary = CustomColor.TextSecondary

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun HomeAccountScreen(
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToAccountDetails: (String) -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
    viewModel: HomeAccountViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeAccountContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNavigateToAccounts = onNavigateToAccounts,
        onNavigateToTransfer = onNavigateToTransfer,
        onNavigateToAccountDetails = onNavigateToAccountDetails,
        onNavigateToMenu = onNavigateToMenu,
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun HomeAccountContent(
    uiState: UiState<HomeData>,
    onRetry: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    onNavigateToAccountDetails: (String) -> Unit = {},
    onNavigateToMenu: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = ArkeoRed,
                errorColor = ArkeoRed,
            ) { homeData ->
                val firstName = homeData.personalInformation.firstname?.firstOrNull()?.uppercaseChar()
                val lastName = homeData.personalInformation.lastname?.uppercase()
                val greeting = when {
                    firstName != null && lastName != null -> "Bonjour $firstName. $lastName"
                    lastName != null -> "Bonjour $lastName"
                    else -> "Bonjour"
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = greeting,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextPrimary,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        MainAccountCard(onNavigateToAccountDetails = onNavigateToAccountDetails, account = homeData.account)
                    }

                    item {
                        SectionRowCard(
                            title = "TOUTE MON ÉPARGNE",
                            onClick = {},
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            } // fin UiStateHandler

            BottomNavBar(
                selectedRoute = Routes.Bank.Home,
                mapItems = mapOf(
                    Routes.Bank.Home to { },
                    Routes.Bank.ListAccount to { onNavigateToAccounts() },
                    Routes.Operation to { onNavigateToTransfer() },
                    Routes.Core.Menu to { onNavigateToMenu() },
                ),
            )
        }
    }
}

@Composable
private fun MainAccountCard(onNavigateToAccountDetails: (String) -> Unit = {}, account: BankAccountDetail) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cardColor = if (isPressed) Color(0xFFF0F0F0) else Color.White

    val accountTypeName = account.type?.name ?: "COMPTE"
    val accountIban = account.iban ?: "—"
    val accountSold = account.sold ?: 0.0
    val overdraftLimit = account.parameter?.overdraftLimit

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { account.id?.let { onNavigateToAccountDetails(it) } },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = accountTypeName,
                color = ArkeoRed,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = accountIban,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = formatAmount(accountSold),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ArkeoRed,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (overdraftLimit != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF5F5F5),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Découvert autorisé :  ${formatAmount(overdraftLimit)}",
                            fontSize = 13.sp,
                            color = TextSecondary,
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                ArkeoQuickAction(icon = Icons.Default.Receipt, label = "Relevés")
                ArkeoQuickAction(icon = Icons.Default.AccountBalance, label = "RIB")
            }
        }
    }
}

@Composable
private fun SectionRowCard(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = title,
                    color = ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ArkeoRed,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

private fun formatAmount(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)

private fun sampleHomeData(): HomeData =
    HomeData(
        account = BankAccountDetail(
            id = "1",
            parameter = BankAccountParameter(),
            type = Type(
                id = 1,
                name = "COMPTE CHÈQUES",
            ),
            sold = 478.27,
            iban = "FR7630006000011234567890140",
        ),
        personalInformation = PersonalInformation(
            id = 1,
            firstname = "Jean",
            lastname = "Dupont",
            email = "jean.dupont@email.com",
        ),
    )

@Preview(showBackground = true, name = "State - Success")
@Composable
fun HomeAccountPreviewSuccess() {
    HomeAccountContent(uiState = UiState.Success(sampleHomeData()))
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun HomeAccountPreviewLoading() {
    HomeAccountContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun HomeAccountPreviewError() {
    HomeAccountContent(uiState = UiState.Error(message = "Impossible de charger les données"))
}
