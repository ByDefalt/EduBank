package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.account.Account
import defalt.domain.entity.bank.State
import defalt.featureBank.viewModel.AdminCreateBankAccountViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

private val BANK_TYPES_CREATE = listOf(1 to "COMPTE CHÈQUES", 2 to "COMPTE ÉPARGNE", 3 to "COMPTE PROFESSIONNEL")

@Composable
fun AdminCreateBankAccountScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: AdminCreateBankAccountViewModel = koinViewModel(),
) {
    val accountsState by viewModel.accountsState.collectAsStateWithLifecycle()
    val createState by viewModel.createState.collectAsStateWithLifecycle()
    val isLoading = createState is UiState.Loading

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        ArkeoTopBar(title = "CRÉER UN COMPTE BANCAIRE", onBack = onBack)

        UiStateHandler(
            uiState = accountsState,
            onRetry = viewModel::retryAccounts,
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { accounts ->
            CreateForm(
                accounts = accounts,
                isLoading = isLoading,
                onSubmit = { accountId, iban, typeId, sold, overdraft, state ->
                    viewModel.create(
                        accountId = accountId,
                        iban = iban,
                        typeId = typeId,
                        sold = sold,
                        overdraftLimit = overdraft,
                        state = state,
                        onSuccess = onSuccess,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateForm(
    accounts: List<Account>,
    isLoading: Boolean,
    onSubmit: (accountId: String, iban: String, typeId: Int, sold: Double, overdraft: Double, state: State) -> Unit,
) {
    var iban by remember { mutableStateOf("") }
    var sold by remember { mutableStateOf("0.0") }
    var overdraft by remember { mutableStateOf("0.0") }
    var selectedTypeId by remember { mutableStateOf(1) }
    var selectedState by remember { mutableStateOf(State.ACTIVE) }
    var selectedAccount by remember { mutableStateOf(accounts.firstOrNull()) }
    var accountExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }
    var stateExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ArkeoCard(title = "NOUVEAU COMPTE") {
            // Sélection du titulaire
            ExposedDropdownMenuBox(expanded = accountExpanded, onExpandedChange = { accountExpanded = !accountExpanded }) {
                OutlinedTextField(
                    value = selectedAccount?.id ?: "Sélectionner un compte",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Titulaire (ID compte)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(accountExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                )
                ExposedDropdownMenu(expanded = accountExpanded, onDismissRequest = { accountExpanded = false }) {
                    accounts.forEach { acc ->
                        DropdownMenuItem(
                            text = { Text(acc.id ?: "-") },
                            onClick = { selectedAccount = acc; accountExpanded = false },
                        )
                    }
                }
            }

            // IBAN
            OutlinedTextField(
                value = iban,
                onValueChange = { iban = it },
                label = { Text("IBAN") },
                modifier = Modifier.fillMaxWidth(),
            )

            // Type de compte
            ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = !typeExpanded }) {
                OutlinedTextField(
                    value = BANK_TYPES_CREATE.find { it.first == selectedTypeId }?.second ?: "Type $selectedTypeId",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Type de compte") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                )
                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                    BANK_TYPES_CREATE.forEach { (id, label) ->
                        DropdownMenuItem(text = { Text(label) }, onClick = { selectedTypeId = id; typeExpanded = false })
                    }
                }
            }

            // Solde initial
            OutlinedTextField(
                value = sold,
                onValueChange = { sold = it },
                label = { Text("Solde initial (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // Découvert autorisé
            OutlinedTextField(
                value = overdraft,
                onValueChange = { overdraft = it },
                label = { Text("Découvert autorisé (€)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            // État initial
            ExposedDropdownMenuBox(expanded = stateExpanded, onExpandedChange = { stateExpanded = !stateExpanded }) {
                OutlinedTextField(
                    value = selectedState.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("État initial") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(stateExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                )
                ExposedDropdownMenu(expanded = stateExpanded, onDismissRequest = { stateExpanded = false }) {
                    State.entries.forEach { state ->
                        DropdownMenuItem(text = { Text(state.value) }, onClick = { selectedState = state; stateExpanded = false })
                    }
                }
            }

            ArkeoButton(
                text = if (isLoading) "Création…" else "CRÉER LE COMPTE",
                onClick = {
                    if (!isLoading && selectedAccount != null && iban.isNotBlank()) {
                        selectedAccount!!.id?.let { onSubmit(it, iban, selectedTypeId, sold.toDoubleOrNull() ?: 0.0, overdraft.toDoubleOrNull() ?: 0.0, selectedState) }
                    }
                },
            )
        }
    }
}
