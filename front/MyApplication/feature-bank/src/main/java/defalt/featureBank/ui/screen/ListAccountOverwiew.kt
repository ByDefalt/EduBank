package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.ui.component.ArkeoButton
import defalt.core.ui.utils.CustomColor
import defalt.core.api.bank.model.BankAccountEntity
import java.util.Locale

@Composable
fun ListAccountOverviewScreen(onBack: () -> Unit) {
    // Liste d'exemple (BankAccountEntity a un constructeur simple)
    val accounts = remember { sampleAccounts() }
    val typeNames = mapOf(1 to "Compte courant", 2 to "Épargne", 3 to "Compte professionnel")

    Column(modifier = Modifier
        .fillMaxSize()
        .background(CustomColor.BackgroundGray)) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.ArkeoRed)
                .padding(top = 48.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "MES COMPTES",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        LazyColumn(modifier = Modifier
            .padding(16.dp)) {
            items(accounts) { account ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier
                                .size(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = CustomColor.BridgeTeal
                        ) {
                            // Placeholder minimal
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = typeNames[account.typeId] ?: "--",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = typeNames[account.typeId] ?: "Type inconnu",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = maskIban(account.iban), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = "Solde : ${formatMoney(account.sold)}", fontSize = 12.sp)
                                Text(text = "ID: ${account.id}", color = CustomColor.ArkeoRed, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                ArkeoButton(text = "Retour", onClick = onBack)
            }
        }
    }
}

private fun sampleAccounts(): List<BankAccountEntity> = listOf(
    BankAccountEntity(id = 1, parameterId = 0, typeId = 1, sold = 1250.50, iban = "FR7630006000011234567890189"),
    BankAccountEntity(id = 2, parameterId = 0, typeId = 2, sold = 5400.00, iban = "FR7630006000019876543210123"),
    BankAccountEntity(id = 3, parameterId = 0, typeId = 3, sold = 10234.75, iban = "FR7630006000015555555555555"),
)

private fun maskIban(iban: String): String {
    val visibleEnd = iban.takeLast(4)
    return "**** **** **** ${visibleEnd}"
}

private fun formatMoney(value: Double): String {
    // Formatage simple en locale FR
    return String.format(Locale.FRANCE, "%.2f €", value)
}

@Preview(showBackground = true)
@Composable
fun ListAccountOverviewPreview() {
    ListAccountOverviewScreen(onBack = {})
}
