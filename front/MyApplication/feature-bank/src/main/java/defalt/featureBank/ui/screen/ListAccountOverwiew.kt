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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.api.bank.model.BankAccountEntity
import defalt.ui.utils.CustomColor
import defalt.ui.component.BottomNavBar
import defalt.ui.utils.Routes
import java.util.Locale

private val ArkeoRed = CustomColor.ArkeoRed
private val LightGray = CustomColor.BackgroundGray
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF666666)

@Composable
fun ListAccountOverviewScreen(
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    val accounts = remember { sampleAccounts() }
    val typeNames = mapOf(
        1 to "COMPTE CHÈQUES 1",
        2 to "COMPTE ÉPARGNE",
        3 to "COMPTE PROFESSIONNEL",
    )

    val totalSold = accounts.sumOf { it.sold }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Header ───────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Mes Comptes au quotidien",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                )
            }

            // ── Total solde ──────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total ${formatMoney(totalSold)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextPrimary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Liste des comptes ────────────────────────────────────────────
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                items(accounts) { account ->
                    AccountCard(
                        account = account,
                        label = typeNames[account.typeId] ?: "COMPTE",
                        onClick = { /* navigation */ },
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // ── Bottom Navigation ────────────────────────────────────────────
            BottomNavBar(
                selectedRoute = Routes.Bank.ListAccount,
                mapItems = mapOf(
                    Routes.Bank.ListAccount to {  },
                    Routes.Bank.Home to { onNavigateToHomeBank() },
                    Routes.Operation to { onNavigateToTransfer() },
                )
            )
        }
    }
}

@Composable
private fun AccountCard(
    account: BankAccountEntity,
    label: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.3.sp,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "N° ${maskAccountNumber(account.iban)}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatMoney(account.sold),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "À venir : ${formatMoney(0.0)}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Voir détails",
                tint = ArkeoRed,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

private fun sampleAccounts(): List<BankAccountEntity> = listOf(
    BankAccountEntity(
        id = 1,
        parameterId = 0,
        typeId = 1,
        sold = 1679138.00,
        iban = "FR7630006000011234567890140",
    ),
    BankAccountEntity(
        id = 2,
        parameterId = 0,
        typeId = 1,
        sold = 459393.44,
        iban = "FR7630006000019876543210140",
    ),
    BankAccountEntity(
        id = 3,
        parameterId = 0,
        typeId = 1,
        sold = 5866841.38,
        iban = "FR7630006000015555555555540",
    ),
    BankAccountEntity(
        id = 4,
        parameterId = 0,
        typeId = 2,
        sold = 775854.79,
        iban = "FR7630006000013333333333340",
    ),
    BankAccountEntity(
        id = 5,
        parameterId = 0,
        typeId = 3,
        sold = 1080899.08,
        iban = "FR7630006000014444444444440",
    ),
)

private fun maskAccountNumber(iban: String): String {
    val last2 = iban.takeLast(2)
    return "XXXXX$last2"
}

private fun formatMoney(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)


@Preview(showBackground = true)
@Composable
fun ListAccountOverviewPreview() {
    ListAccountOverviewScreen()
}
