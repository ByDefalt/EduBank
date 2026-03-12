package defalt.featureOffer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
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
import defalt.domain.entity.offer.OfferInput
import defalt.featureOffer.viewModel.AdminCreateOfferViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoTopBar
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.time.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminCreateOfferScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onMutationSuccess: () -> Unit = {},
    viewModel: AdminCreateOfferViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) { viewModel.onMutationSuccess = onMutationSuccess }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) onSuccess()
    }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        ArkeoTopBar(title = "NOUVELLE OFFRE", onBack = onBack)

        CreateOfferForm(
            isLoading = uiState is UiState.Loading,
            onCreate = { title, desc, state -> viewModel.create(title, desc, state, LocalDate.now(), LocalDate.now().plusMonths(1)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateOfferForm(isLoading: Boolean, onCreate: (String, String, OfferInput.State) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf(OfferInput.State.ACTIVE) }
    var stateExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        ArkeoCard(title = "NOUVELLE OFFRE") {
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
                    listOf(OfferInput.State.ACTIVE, OfferInput.State.INACTIVE).forEach { s ->
                        DropdownMenuItem(text = { Text(s.value) }, onClick = { selectedState = s; stateExpanded = false })
                    }
                }
            }

            ArkeoButton(
                text = if (isLoading) "Création…" else "CRÉER L'OFFRE",
                onClick = { if (!isLoading && title.isNotBlank() && description.isNotBlank()) onCreate(title, description, selectedState) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() { AdminCreateOfferScreen() }
