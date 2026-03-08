package defalt.featureOperation.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.viewModel.AdminOperationDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.time.format.DateTimeFormatter
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminOperationDetailScreen(
    id: Int,
    onBack: () -> Unit = {},
    viewModel: AdminOperationDetailViewModel = koinViewModel(),
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
            Text("DÉTAIL OPÉRATION", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CustomColor.TextPrimary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }

        UiStateHandler(
            uiState = uiState,
            onRetry = { viewModel.load(id) },
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { operation ->
            val isLoading = actionState is UiState.Loading
            val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // UC8 : Infos
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("INFORMATIONS", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        OpLabelValue("ID", operation.id.toString())
                        OpLabelValue("Libellé", operation.label)
                        OpLabelValue("Montant", "%.2f €".format(operation.amount))
                        OpLabelValue("IBAN cible", operation.ibanTarget)
                        OpLabelValue("Compte source", operation.accountSourceId)
                        OpLabelValue("Date", operation.date.format(fmt))
                        OpLabelValue("État", operation.state.value)
                    }
                }

                // UC13/UC21 : Changer état
                ChangeStateCard(
                    currentState = operation.state,
                    isLoading = isLoading,
                    onChangeState = { newState -> viewModel.updateState(id, newState) },
                )

                // UC20 : Annuler
                if (operation.state == OperationState.PENDING) {
                    OutlinedButton(
                        onClick = { if (!isLoading) viewModel.cancel(id) },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomColor.ArkeoRed),
                        border = BorderStroke(1.5.dp, CustomColor.ArkeoRed),
                    ) {
                        Text(if (isLoading) "En cours…" else "ANNULER L'OPÉRATION", fontWeight = FontWeight.Bold, color = CustomColor.ArkeoRed)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeStateCard(
    currentState: OperationState,
    isLoading: Boolean,
    onChangeState: (OperationState) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember(currentState) { mutableStateOf(currentState) }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("MODIFIER L'ÉTAT", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = selected.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("État") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    OperationState.entries.forEach { state ->
                        DropdownMenuItem(
                            text = { Text(state.value) },
                            onClick = { selected = state; expanded = false },
                        )
                    }
                }
            }
            ArkeoButton(
                text = if (isLoading) "En cours…" else "APPLIQUER",
                onClick = { if (!isLoading) onChangeState(selected) },
            )
        }
    }
}

@Composable
private fun OpLabelValue(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text("$label :", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, modifier = Modifier.weight(0.4f), color = CustomColor.TextPrimary)
        Text(value, fontSize = 13.sp, modifier = Modifier.weight(0.6f), color = CustomColor.TextPrimary)
    }
}
