package defalt.featureBank.ui.screen

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.featureBank.viewModel.AdminBankDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminBankDetailScreen(
    id: String,
    onBack: () -> Unit = {},
    viewModel: AdminBankDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = CustomColor.ArkeoRed)
            }
            Text(
                "COMPTE BANCAIRE",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CustomColor.TextPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        UiStateHandler(
            uiState = uiState,
            onRetry = { viewModel.load(id) },
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { detail ->
            var overdraft by remember { mutableStateOf(detail.parameter?.overdraftLimit?.toString() ?: "0.0") }
            val isLoading = actionState is UiState.Loading

            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Infos
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("INFORMATIONS", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        BankLabelValue("ID", detail.id ?: "-")
                        BankLabelValue("IBAN", detail.iban ?: "-")
                        BankLabelValue("Type", detail.type?.name ?: "-")
                        BankLabelValue("Solde", "%.2f €".format(detail.sold ?: 0.0))
                        BankLabelValue("État param.", detail.parameter?.state?.name ?: "-")
                    }
                }

                // Édition
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("MODIFIER PARAMÈTRE", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OutlinedTextField(
                            value = overdraft,
                            onValueChange = { overdraft = it },
                            label = { Text("Découvert autorisé (€)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        ArkeoButton(
                            text = if (isLoading) "Enregistrement…" else "ENREGISTRER",
                            onClick = { if (!isLoading) viewModel.update(id, overdraft.toDoubleOrNull() ?: 0.0) },
                        )
                    }
                }

                // Suppression
                OutlinedButton(
                    onClick = { if (!isLoading) viewModel.delete(id, onBack) },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomColor.ArkeoRed),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CustomColor.ArkeoRed),
                ) {
                    Text("SUPPRIMER CE COMPTE", fontWeight = FontWeight.Bold, color = CustomColor.ArkeoRed)
                }
            }
        }
    }
}

@Composable
private fun BankLabelValue(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("$label :", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(0.4f), color = CustomColor.TextPrimary)
        Text(value, fontSize = 13.sp, modifier = Modifier.weight(0.6f), color = CustomColor.TextPrimary)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AdminBankDetailScreen(id = "bank-0001")
}
