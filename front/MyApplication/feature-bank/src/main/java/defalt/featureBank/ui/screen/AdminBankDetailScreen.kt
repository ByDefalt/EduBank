package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.bank.State
import defalt.domain.entity.bank.Type as BankAccountType
import defalt.featureBank.viewModel.AdminBankDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoLabelValue
import defalt.ui.component.ArkeoOutlinedButton
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminBankDetailScreen(
    id: String,
    onBack: () -> Unit = {},
    onMutationSuccess: () -> Unit = {},
    viewModel: AdminBankDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()
    val typesState by viewModel.typesState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    // Branche le callback du graph vers le ViewModel
    LaunchedEffect(Unit) { viewModel.onMutationSuccess = onMutationSuccess }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        ArkeoTopBar(title = "COMPTE BANCAIRE", onBack = onBack)

        UiStateHandler(
            uiState = uiState,
            onRetry = { viewModel.load(id) },
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { detail ->
            val isLoading = actionState is UiState.Loading

            var overdraft by rememberSaveable(detail.id) {
                mutableStateOf(detail.parameter?.overdraftLimit?.toString() ?: "0.0")
            }
            var selectedTypeId by rememberSaveable(detail.id) {
                mutableIntStateOf(detail.type?.id ?: 1)
            }
            var selectedState by rememberSaveable(detail.id) {
                mutableStateOf(detail.parameter?.state ?: State.ACTIVE)
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // ── Infos ──────────────────────────────────────────────────
                ArkeoCard(title = "INFORMATIONS") {
                    ArkeoLabelValue("ID", detail.id ?: "-")
                    ArkeoLabelValue("IBAN", detail.iban ?: "-")
                    ArkeoLabelValue("Type actuel", detail.type?.name ?: "-")
                    ArkeoLabelValue("Solde", "%.2f €".format(detail.sold ?: 0.0))
                    ArkeoLabelValue("Découvert autorisé", "%.2f €".format(detail.parameter?.overdraftLimit ?: 0.0))
                    ArkeoLabelValue("État actuel", detail.parameter?.state?.name ?: "-")
                }

                // ── Modification complète ──────────────────────────────────
                ArkeoCard(title = "MODIFIER LE COMPTE") {
                    when (val ts = typesState) {
                        is UiState.Loading -> Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center,
                        ) { CircularProgressIndicator(color = CustomColor.ArkeoRed) }

                        is UiState.Success -> TypeDropdown(
                            types = ts.data,
                            selectedTypeId = selectedTypeId,
                            onTypeSelected = { selectedTypeId = it },
                        )

                        else -> TypeDropdown(
                            types = emptyList(),
                            selectedTypeId = selectedTypeId,
                            onTypeSelected = { selectedTypeId = it },
                        )
                    }

                    StateDropdown(
                        selectedState = selectedState,
                        onStateSelected = { selectedState = it },
                    )

                    OutlinedTextField(
                        value = overdraft,
                        onValueChange = { overdraft = it },
                        label = { Text("Découvert autorisé (€)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    ArkeoButton(
                        text = if (isLoading) "Enregistrement…" else "ENREGISTRER",
                        onClick = {
                            if (!isLoading) {
                                val normalized = overdraft.replace(',', '.')
                                viewModel.updateFull(
                                    id = id,
                                    typeId = selectedTypeId,
                                    overdraftLimit = normalized.toDoubleOrNull() ?: 0.0,
                                    state = selectedState,
                                )
                            }
                        },
                    )
                }

                // ── Suppression ───────────────────────────────────────────
                ArkeoOutlinedButton(
                    text = "SUPPRIMER CE COMPTE",
                    onClick = { if (!isLoading) viewModel.delete(id, onBack) },
                    enabled = !isLoading,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypeDropdown(
    types: List<BankAccountType>,
    selectedTypeId: Int,
    onTypeSelected: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = types.find { it.id == selectedTypeId }?.name ?: "Type $selectedTypeId"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type de compte") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            if (types.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("Aucun type disponible") },
                    onClick = { expanded = false },
                    enabled = false,
                )
            } else {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { type.name?.let { Text(it) } },
                        onClick = {
                            type.id?.let { onTypeSelected(it) }
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StateDropdown(selectedState: State, onStateSelected: (State) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedState.value,
            onValueChange = {},
            readOnly = true,
            label = { Text("État du compte") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            State.entries.forEach { state ->
                DropdownMenuItem(
                    text = { Text(state.value) },
                    onClick = {
                        onStateSelected(state)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AdminBankDetailScreen(id = "bank-0001")
}
