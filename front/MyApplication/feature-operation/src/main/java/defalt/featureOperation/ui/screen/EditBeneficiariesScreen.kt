package defalt.featureOperation.ui.screen

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
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import defalt.featureOperation.viewModel.EditBeneficiaryViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoInput
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = safeBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = CustomColor.ArkeoRed,
                )
            }
            Text(
                text = "MODIFIER LE BÉNÉFICIAIRE",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = CustomColor.TextPrimary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "INFORMATIONS DU BÉNÉFICIAIRE",
                    color = CustomColor.ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

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

                OutlinedButton(
                    onClick = safeDelete,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = CustomColor.ArkeoRed,
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CustomColor.ArkeoRed),
                ) {
                    Text(
                        text = "SUPPRIMER LE BÉNÉFICIAIRE",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomColor.ArkeoRed,
                    )
                }
            }
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
