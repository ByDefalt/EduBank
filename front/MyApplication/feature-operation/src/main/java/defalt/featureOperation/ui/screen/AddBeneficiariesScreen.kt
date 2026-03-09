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
import defalt.featureOperation.viewModel.AddBeneficiaryViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoCard
import defalt.ui.component.ArkeoInput
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun AddBeneficiaryScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: AddBeneficiaryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Success) onSuccess()
    }

    AddBeneficiaryContent(
        isLoading = uiState is UiState.Loading,
        onBack = onBack,
        onConfirm = { name, iban -> viewModel.add(name, iban) },
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun AddBeneficiaryContent(
    isLoading: Boolean = false,
    onBack: () -> Unit = {},
    onConfirm: (name: String, iban: String) -> Unit = { _, _ -> },
) {
    var name by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }

    val safeBack = safeClick(onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray)
            .verticalScroll(rememberScrollState()),
    ) {
        ArkeoTopBar(title = "AJOUTER UN BÉNÉFICIAIRE", onBack = safeBack)

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
                text = if (isLoading) "Ajout en cours…" else "AJOUTER LE BÉNÉFICIAIRE",
                onClick = { if (!isLoading) onConfirm(name, iban) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddBeneficiaryScreenPreview() {
    AddBeneficiaryContent()
}
