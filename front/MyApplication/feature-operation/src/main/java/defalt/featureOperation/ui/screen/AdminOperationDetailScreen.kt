package defalt.featureOperation.ui.screen

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.operation.OperationState
import defalt.featureOperation.viewModel.AdminOperationDetailViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoLabelValue
import defalt.ui.component.ArkeoOutlinedButton
import defalt.ui.component.ArkeoTopBar
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
        ArkeoTopBar(title = "DÉTAIL OPÉRATION", onBack = onBack)

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
                ArkeoCard(title = "INFORMATIONS") {
                    ArkeoLabelValue("ID", operation.id.toString())
                    ArkeoLabelValue("Libellé", operation.label)
                    ArkeoLabelValue("Montant", "%.2f €".format(operation.amount))
                    ArkeoLabelValue("IBAN cible", operation.ibanTarget)
                    ArkeoLabelValue("Compte source", operation.accountSourceId)
                    ArkeoLabelValue("Date", operation.date.format(fmt))
                    ArkeoLabelValue("État", operation.state.value)
                }

                // UC13/UC21 : Changer état
                ChangeStateCard(
                    currentState = operation.state,
                    isLoading = isLoading,
                    onChangeState = { newState -> viewModel.updateState(id, newState) },
                )

                // UC20 : Annuler
                if (operation.state == OperationState.PENDING) {
                    ArkeoOutlinedButton(
                        text = if (isLoading) "En cours…" else "ANNULER L'OPÉRATION",
                        onClick = { if (!isLoading) viewModel.cancel(id) },
                        enabled = !isLoading,
                    )
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

    ArkeoCard(title = "MODIFIER L'ÉTAT") {
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
