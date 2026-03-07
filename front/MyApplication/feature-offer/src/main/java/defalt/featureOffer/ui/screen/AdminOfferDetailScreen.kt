package defalt.featureOffer.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.featureOffer.viewModel.AdminOfferDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.time.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminOfferDetailScreen(
    id: Int,
    onBack: () -> Unit = {},
    viewModel: AdminOfferDetailViewModel = koinViewModel(),
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
            Text("MODIFIER L'OFFRE", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = CustomColor.TextPrimary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(48.dp))
        }

        UiStateHandler(uiState = uiState, onRetry = { viewModel.load(id) }, loadingColor = CustomColor.ArkeoRed, errorColor = CustomColor.ArkeoRed) { offer ->
            OfferDetailForm(
                offer = offer,
                isLoading = actionState is UiState.Loading,
                onSave = { title, desc, state, start, end -> viewModel.save(id, title, desc, state, start, end) },
                onDelete = { viewModel.delete(id, onBack) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OfferDetailForm(
    offer: Offer,
    isLoading: Boolean,
    onSave: (String, String, OffersIdPutRequest.State, LocalDate, LocalDate) -> Unit,
    onDelete: () -> Unit,
) {
    var title by remember { mutableStateOf(offer.title) }
    var description by remember { mutableStateOf(offer.description) }
    var selectedState by remember { mutableStateOf(OffersIdPutRequest.State.valueOf(offer.state.name)) }
    var stateExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("DÉTAILS DE L'OFFRE", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

                ExposedDropdownMenuBox(expanded = stateExpanded, onExpandedChange = { stateExpanded = it }) {
                    OutlinedTextField(
                        value = selectedState.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("État") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(stateExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                    )
                    ExposedDropdownMenu(expanded = stateExpanded, onDismissRequest = { stateExpanded = false }) {
                        OffersIdPutRequest.State.entries.forEach { s ->
                            DropdownMenuItem(text = { Text(s.value) }, onClick = { selectedState = s; stateExpanded = false })
                        }
                    }
                }

                ArkeoButton(text = if (isLoading) "Enregistrement…" else "ENREGISTRER", onClick = {
                    if (!isLoading) onSave(title, description, selectedState, offer.startDate, offer.endDate)
                })
            }
        }

        OutlinedButton(
            onClick = { if (!isLoading) onDelete() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomColor.ArkeoRed),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CustomColor.ArkeoRed),
        ) { Text("SUPPRIMER L'OFFRE", fontWeight = FontWeight.Bold, color = CustomColor.ArkeoRed) }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() { AdminOfferDetailScreen(id = 1) }
