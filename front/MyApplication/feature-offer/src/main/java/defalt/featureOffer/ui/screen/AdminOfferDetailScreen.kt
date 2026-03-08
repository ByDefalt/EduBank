package defalt.featureOffer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.offer.Offer
import defalt.domain.entity.offer.OffersIdPutRequest
import defalt.featureOffer.viewModel.AdminOfferDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoOutlinedButton
import defalt.ui.component.ArkeoTopBar
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
        ArkeoTopBar(title = "MODIFIER L'OFFRE", onBack = onBack)

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
        ArkeoCard(title = "DÉTAILS DE L'OFFRE") {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            ExposedDropdownMenuBox(expanded = stateExpanded, onExpandedChange = { stateExpanded = it }) {
                OutlinedTextField(
                    value = selectedState.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("État") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(stateExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
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

        ArkeoOutlinedButton(
            text = "SUPPRIMER L'OFFRE",
            onClick = { if (!isLoading) onDelete() },
            enabled = !isLoading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() { AdminOfferDetailScreen(id = 1) }
