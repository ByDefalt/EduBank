package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Operation
import defalt.domain.entity.operation.OperationState
import defalt.featureBank.viewModel.AccountDetailsData
import defalt.featureBank.viewModel.AccountDetailsViewModel
import defalt.ui.component.BottomNavBar
import defalt.ui.component.UiStateHandler
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import org.koin.androidx.compose.koinViewModel

private val ArkeoRed = CustomColor.ArkeoRed
private val LightGray = CustomColor.BackgroundGray
private val TextPrimary = CustomColor.TextPrimary
private val TextSecondary = CustomColor.TextSecondary

private const val LABEL_MAX_CHARS = 20

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun AccountDetailsScreen(
    accountId: Int = 1,
    accountLabel: String = "COMPTE CHÈQUES 1",
    onNavigateBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    viewModel: AccountDetailsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AccountDetailsContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        accountLabel = accountLabel,
        onNavigateBack = onNavigateBack,
        onNavigateToHomeBank = onNavigateToHomeBank,
        onNavigateToAccounts = onNavigateToAccounts,
        onNavigateToTransfer = onNavigateToTransfer,
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun AccountDetailsContent(
    uiState: UiState<AccountDetailsData>,
    onRetry: () -> Unit = {},
    accountLabel: String = "COMPTE CHÈQUES 1",
    onNavigateBack: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var showFullLabel by rememberSaveable { mutableStateOf(false) }
    val tabs = listOf("Comptabilisées", "À venir")

    val safeNavigateBack = safeClick(onNavigateBack)
    val safeNavigateHome = safeClick(onNavigateToHomeBank)
    val safeNavigateAccounts = safeClick(onNavigateToAccounts)
    val safeNavigateTransfer = safeClick(onNavigateToTransfer)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(title = accountLabel, onNavigateBack = safeNavigateBack)

            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = ArkeoRed,
                errorColor = ArkeoRed,
            ) { data ->
                val operations = data.operations
                val account = data.account

                val filteredOperations = remember(operations, selectedTab) {
                    when (selectedTab) {
                        0 -> operations.filter { it.state == OperationState.COMPLETED }
                        else -> operations.filter {
                            it.state == OperationState.PENDING || it.state == OperationState.CANCELLED
                        }
                    }
                }

                val groupedOperations = remember(filteredOperations) {
                    filteredOperations
                        .sortedByDescending { it.date }
                        .groupBy { it.date.toLocalDate() }
                        .entries
                        .sortedByDescending { it.key }
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(12.dp)) }

                    item { AccountSummaryCard(account = account) }

                    item {
                        OperationsTabBar(
                            tabs = tabs,
                            selectedIndex = selectedTab,
                            onTabSelected = { selectedTab = it },
                        )
                    }

                    item {
                        LabelToggleRow(
                            checked = showFullLabel,
                            onCheckedChange = { showFullLabel = it },
                        )
                    }

                    if (groupedOperations.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "Aucune opération",
                                    color = TextSecondary,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    } else {
                        groupedOperations.forEach { (date, ops) ->
                            item(key = "header_${selectedTab}_$date") {
                                DateSeparator(label = formatDateHeader(date))
                            }
                            item(key = "group_${selectedTab}_$date") {
                                OperationsGroupCard(operations = ops, showFullLabel = showFullLabel)
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            } // fin UiStateHandler

            BottomNavBar(
                selectedRoute = Routes.Bank.ListAccount,
                mapItems = mapOf(
                    Routes.Bank.Home to safeNavigateHome,
                    Routes.Bank.ListAccount to safeNavigateAccounts,
                    Routes.Operation to safeNavigateTransfer,
                ),
            )
        }
    }
}

@Composable
private fun Header(title: String, onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = ArkeoRed,
            )
        }
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun AccountSummaryCard(account: BankAccount) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formatAmount(account.sold),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Découvert autorisé : ${formatAmount(0.0)}",
                color = TextSecondary,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                QuickActionDetail(icon = Icons.Default.Receipt, label = "Relevés")
                QuickActionDetail(icon = Icons.Default.AccountBalance, label = "RIB")
            }
        }
    }
}

@Composable
private fun OperationsTabBar(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        tabs.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                    color = if (isSelected) ArkeoRed else TextSecondary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            color = if (isSelected) ArkeoRed else Color(0xFFDDDDDD),
                            shape = RoundedCornerShape(2.dp),
                        ),
                )
            }
        }
    }
}

@Composable
private fun LabelToggleRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = ArkeoRed,
                uncheckedColor = TextSecondary,
                checkmarkColor = Color.White,
            ),
        )
        Text(
            text = "Afficher les libellés longs",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}

@Composable
private fun DateSeparator(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 10.dp),
            letterSpacing = 0.5.sp,
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFDDDDDD))
    }
}

@Composable
private fun OperationsGroupCard(operations: List<Operation>, showFullLabel: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            operations.forEachIndexed { index, operation ->
                OperationRow(operation = operation, showFullLabel = showFullLabel)
                if (index < operations.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF0F0F0),
                    )
                }
            }
        }
    }
}

@Composable
private fun OperationRow(operation: Operation, showFullLabel: Boolean) {
    val isCredit = operation.amount > 0
    val amountColor = if (isCredit) Color(0xFF2E7D32) else Color(0xFFCC0000)
    val displayLabel = if (showFullLabel || operation.label.length <= LABEL_MAX_CHARS) {
        operation.label
    } else {
        operation.label.take(LABEL_MAX_CHARS) + "…"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (isCredit) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(10.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = amountColor,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = displayLabel,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "${if (isCredit) "+" else ""}${formatAmount(operation.amount)}",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = amountColor,
        )
    }
}

@Composable
private fun QuickActionDetail(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { },
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

private fun formatAmount(value: Double?): String =
    String.format(Locale.FRANCE, "%.2f €", value)

private fun formatDateHeader(date: LocalDate): String {
    val today = LocalDate.now()
    val pattern = if (date.year == today.year) "d MMMM" else "d MMMM yyyy"
    return date.format(DateTimeFormatter.ofPattern(pattern, Locale.FRANCE))
        .replaceFirstChar { it.uppercase() }
}

private fun sampleAccount() = BankAccount(
    id = "1",
    parameterId = 0,
    typeId = 1,
    sold = 478.27,
    iban = "FR7630006000011234567890140",
)

private fun sampleOperations(): List<Operation> {
    val now = OffsetDateTime.now()
    return listOf(
        Operation(1, "1", "Virement reçu - Salaire", OperationState.COMPLETED, "FR76300060000198", 2350.00, now.minusDays(0).withHour(9).withMinute(0)),
        Operation(2, "1", "Paiement en ligne", OperationState.COMPLETED, "FR76300060000155", -49.99, now.minusDays(0).withHour(14).withMinute(30)),
        Operation(3, "1", "Virement vers épargne", OperationState.COMPLETED, "FR76300060000133", -500.00, now.minusDays(1).withHour(11).withMinute(15)),
        Operation(4, "1", "Remboursement ami", OperationState.COMPLETED, "FR76300060000144", 120.00, now.minusDays(1).withHour(18).withMinute(45)),
        Operation(5, "1", "Abonnement streaming", OperationState.COMPLETED, "FR76300060000111", -14.99, now.minusDays(3).withHour(8).withMinute(0)),
        Operation(6, "1", "Courses alimentaires", OperationState.COMPLETED, "FR76300060000122", -87.50, now.minusDays(3).withHour(17).withMinute(20)),
        Operation(7, "1", "Loyer", OperationState.COMPLETED, "FR76300060000166", -900.00, now.minusDays(5).withHour(7).withMinute(0)),
        Operation(8, "1", "Prélèvement assurance", OperationState.PENDING, "FR76300060000177", -32.50, now.plusDays(2).withHour(10).withMinute(0)),
        Operation(9, "1", "Virement programmé", OperationState.PENDING, "FR76300060000188", -250.00, now.plusDays(2).withHour(12).withMinute(0)),
        Operation(10, "1", "Remboursement prévu", OperationState.PENDING, "FR76300060000199", 75.00, now.plusDays(5).withHour(9).withMinute(0)),
    )
}

@Preview(showBackground = true, name = "State - Success")
@Composable
fun AccountDetailsPreviewSuccess() {
    AccountDetailsContent(
        uiState = UiState.Success(
            AccountDetailsData(
                account = sampleAccount(),
                operations = sampleOperations(),
            ),
        ),
    )
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun AccountDetailsPreviewLoading() {
    AccountDetailsContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun AccountDetailsPreviewError() {
    AccountDetailsContent(uiState = UiState.Error(message = "Impossible de charger le compte"))
}
