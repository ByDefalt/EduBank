package defalt.featureOperation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Euro
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoInput
import defalt.ui.component.safeClick
import defalt.ui.utils.CustomColor

@Composable
fun CreateTransferAmountScreen(
    onBack: () -> Unit = {},
    onNext: (amount: String) -> Unit = {},
) {
    var amount by remember { mutableStateOf("") }

    val safeBack = safeClick(onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        // ── Header avec retour arrière ────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = safeBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = CustomColor.ArkeoRed,
                )
            }
            Text(
                text = "MONTANT DU VIREMENT",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CustomColor.TextPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        // ── Formulaire ────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "SAISIR LE MONTANT",
                    color = CustomColor.ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                ArkeoInput(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Montant (€)",
                    icon = Icons.Outlined.Euro,
                    keyboardType = KeyboardType.Decimal,
                )

                Spacer(modifier = Modifier.height(4.dp))

                ArkeoButton(
                    text = "SUIVANT",
                    onClick = { onNext(amount) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTransferAmountScreenPreview() {
    CreateTransferAmountScreen()
}
