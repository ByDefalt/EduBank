package defalt.featureOperation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.featureOperation.viewModel.EditBeneficiaryViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoInput
import defalt.ui.component.ArkeoOutlinedButton
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun EditBeneficiaryScreen(
    id: Int,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: EditBeneficiaryViewModel = koinViewModel(),
) {
    val beneficiaryState by viewModel.beneficiary.collectAsStateWithLifecycle()
    val actionState by viewModel.actionState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    LaunchedEffect(actionState) {
        if (actionState is UiState.Success) onSuccess()
    }

    UiStateHandler(
        uiState = beneficiaryState,
        onRetry = { viewModel.load(id) },
        loadingColor = CustomColor.ArkeoRed,
        errorColor = CustomColor.ArkeoRed,
    ) { beneficiary ->
        EditBeneficiaryContent(
            initialName = beneficiary.name,
            initialIban = beneficiary.ibanTarget,
            isLoading = actionState is UiState.Loading,
            onBack = onBack,
            onSave = { name, iban -> viewModel.save(id, name, iban, beneficiary.accountSourceId) },
            onDelete = { viewModel.delete(id) },
        )
    }
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun EditBeneficiaryContent(
    initialName: String = "",
    initialIban: String = "",
    isLoading: Boolean = false,
    onBack: () -> Unit = {},
    onSave: (name: String, iban: String) -> Unit = { _, _ -> },
    onDelete: () -> Unit = {},
) {
    var name by remember { mutableStateOf(initialName) }
    var iban by remember { mutableStateOf(initialIban) }

    val safeBack = safeClick(onBack)
    val safeDelete = safeClick(onDelete)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray)
            .verticalScroll(rememberScrollState()),
    ) {
        ArkeoTopBar(title = "MODIFIER LE BÉNÉFICIAIRE", onBack = safeBack)

        ArkeoCard(
            title = "INFORMATIONS DU BÉNÉFICIAIRE",
            modifier = Modifier.padding(16.dp),
        ) {
            ArkeoInput(
                value = name,
                onValueChange = { name = it },
                label = "Nom du bénéficiaire",
                icon = Icons.Outlined.Person,
            )
            ArkeoInput(
                value = iban,
                onValueChange = { iban = it },
                label = "IBAN",
                icon = Icons.Outlined.CreditCard,
                keyboardType = KeyboardType.Ascii,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ArkeoButton(
                text = if (isLoading) "Enregistrement…" else "ENREGISTRER LES MODIFICATIONS",
                onClick = { if (!isLoading) onSave(name, iban) },
            )
            ArkeoOutlinedButton(
                text = "SUPPRIMER LE BÉNÉFICIAIRE",
                onClick = safeDelete,
                enabled = !isLoading,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditBeneficiaryScreenPreview() {
    EditBeneficiaryContent(
        initialName = "Alice Dupont",
        initialIban = "FR76 1234 5678 9012",
    )
}
