package defalt.featureOperation.ui.screen.transfer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.featureOperation.viewModel.CreateTransferViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun CreateTransferRecapScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: CreateTransferViewModel = koinViewModel(),
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    val submitState by viewModel.submitUiState.collectAsStateWithLifecycle()

    // Naviguer vers la suite dès que la soumission réussit
    LaunchedEffect(submitState) {
        if (submitState is UiState.Success) {
            viewModel.reset()
            onSuccess()
        }
    }

    val typeNames = mapOf(
        1 to "COMPTE CHÈQUES",
        2 to "COMPTE ÉPARGNE",
        3 to "COMPTE PROFESSIONNEL",
    )

    CreateTransferRecapContent(
        sourceAccountLabel = typeNames[form.sourceAccount?.typeId] ?: "COMPTE",
        sourceAccountIban = form.sourceAccount?.iban ?: "",
        receiverName = form.receiverName ?: "",
        receiverIban = form.receiverIban ?: "",
        amount = form.amount.toDoubleOrNull() ?: 0.0,
        label = form.label,
        isSubmitting = submitState is UiState.Loading,
        onBack = safeClick(onBack),
        onConfirm = viewModel::submitTransfer,
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun CreateTransferRecapContent(
    sourceAccountLabel: String = "",
    sourceAccountIban: String = "",
    receiverName: String = "",
    receiverIban: String = "",
    amount: Double = 0.0,
    label: String = "",
    isSubmitting: Boolean = false,
    onBack: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val safeConfirm = safeClick(onConfirm)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray)
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Header avec retour arrière ────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = CustomColor.ArkeoRed,
                )
            }
            Text(
                text = "RÉCAPITULATIF",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CustomColor.TextPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        // ── Carte récapitulatif ───────────────────────────────────────────
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "DÉTAILS DU VIREMENT",
                    color = CustomColor.ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Compte à débiter
                RecapRow(label = "Compte débité", value = sourceAccountLabel)
                RecapRow(label = "IBAN source", value = sourceAccountIban)

                RecapDivider()

                // Destinataire
                RecapRow(label = "Bénéficiaire", value = receiverName)
                RecapRow(label = "IBAN destinataire", value = receiverIban)

                RecapDivider()

                // Montant & libellé
                RecapRow(
                    label = "Montant",
                    value = String.format(Locale.FRANCE, "%.2f €", amount),
                    valueColor = CustomColor.ArkeoRed,
                    valueBold = true,
                    valueFontSize = 22,
                )
                RecapRow(label = "Libellé", value = label)

                Spacer(modifier = Modifier.height(24.dp))

                ArkeoButton(
                    text = if (isSubmitting) "EN COURS…" else "EFFECTUER LE VIREMENT",
                    onClick = safeConfirm,
                    enabled = !isSubmitting,
                )
            }
        }
    }
}

// ── Composants internes ───────────────────────────────────────────────────────

@Composable
private fun RecapRow(
    label: String,
    value: String,
    valueColor: Color = CustomColor.TextPrimary,
    valueBold: Boolean = false,
    valueFontSize: Int = 15,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = CustomColor.TextSecondary,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = value.ifBlank { "—" },
            fontSize = valueFontSize.sp,
            fontWeight = if (valueBold) FontWeight.Bold else FontWeight.Normal,
            color = valueColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f),
        )
    }
}

@Composable
private fun RecapDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = Color(0xFFEEEEEE),
    )
}

@Preview(showBackground = true)
@Composable
fun CreateTransferRecapScreenPreview() {
    CreateTransferRecapContent(
        sourceAccountLabel = "COMPTE CHÈQUES",
        sourceAccountIban = "FR76 3000 6000 0112 3456 7890 140",
        receiverName = "Alice Dupont",
        receiverIban = "FR76 1234 5678 9012 3456 7890 123",
        amount = 250.00,
        label = "Remboursement loyer",
    )
}
