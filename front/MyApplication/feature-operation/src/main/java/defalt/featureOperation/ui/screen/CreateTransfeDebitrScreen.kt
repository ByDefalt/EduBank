package defalt.featureOperation.ui.screen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.domain.entity.bank.BankAccount
import defalt.ui.utils.CustomColor
import java.util.Locale

private val ArkeoRed = CustomColor.ArkeoRed
private val TextPrimary = CustomColor.TextPrimary
private val TextSecondary = CustomColor.TextSecondary

@Composable
fun CreateTransferDebitScreen(
    accounts: List<BankAccount> = sampleTransferAccounts(),
    onConfirm: (sourceAccountId: String, beneficiaryName: String, amount: String, motif: String) -> Unit = { _, _, _, _ -> },
) {
    var selectedAccountId by remember { mutableStateOf<String?>(null) }
    var beneficiary by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var motif by remember { mutableStateOf("") }

    val typeNames = mapOf(
        1 to "COMPTE CHÈQUES",
        2 to "COMPTE ÉPARGNE",
        3 to "COMPTE PROFESSIONNEL",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Header titre seul (sans retour arrière) ──────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "NOUVEAUX VIREMENT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                // ── Section : sélection du compte à débiter ──────────────────
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Compte à débiter",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ArkeoRed,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(accounts) { account ->
                    TransferAccountCard(
                        account = account,
                        label = typeNames[account.typeId] ?: "COMPTE",
                        isSelected = account.id == selectedAccountId,
                        onClick = { selectedAccountId = account.id },
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun TransferAccountCard(
    account: BankAccount,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderColor = if (isSelected) ArkeoRed else Color.Transparent
    val cardColor = when {
        isSelected -> Color(0xFFFFF5F5)
        isPressed -> Color(0xFFF0F0F0)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            color = borderColor,
        ),
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
                    text = "N° ${maskIban(account.iban)}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatAmount(account.sold ?: 0.0),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimary,
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Sélectionné",
                    tint = ArkeoRed,
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Sélectionner",
                    tint = TextSecondary,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

private fun maskIban(iban: String?): String {
    val last2 = iban?.takeLast(2) ?: "XX"
    return "XXXXX$last2"
}

private fun formatAmount(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)

private fun sampleTransferAccounts(): List<BankAccount> = listOf(
    BankAccount(id = "1", parameterId = 0, typeId = 1, sold = 1679138.00, iban = "FR7630006000011234567890140"),
    BankAccount(id = "2", parameterId = 0, typeId = 2, sold = 775854.79, iban = "FR7630006000013333333333340"),
    BankAccount(id = "3", parameterId = 0, typeId = 3, sold = 1080899.08, iban = "FR7630006000014444444444440"),
)

@Preview(showBackground = true)
@Composable
fun CreateTransferScreenPreview() {
    CreateTransferDebitScreen()
}
