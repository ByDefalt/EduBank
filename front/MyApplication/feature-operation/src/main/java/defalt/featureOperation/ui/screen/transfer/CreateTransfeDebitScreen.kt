package defalt.featureOperation.ui.screen.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.bank.BankAccountDetail
import defalt.featureOperation.viewModel.CreateTransferViewModel
import defalt.featureOperation.viewModel.DebitStepData
import defalt.featureOperation.viewModel.sampleTransferAccounts
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.util.Locale
import org.koin.androidx.compose.koinViewModel

private val ArkeoRed = CustomColor.ArkeoRed
private val TextPrimary = CustomColor.TextPrimary
private val TextSecondary = CustomColor.TextSecondary

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun CreateTransferDebitScreen(
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: CreateTransferViewModel = koinViewModel(),
) {
    val uiState by viewModel.debitUiState.collectAsStateWithLifecycle()
    CreateTransferDebitContent(
        uiState = uiState,
        onRetry = viewModel::retryDebit,
        onAccountSelected = { account ->
            viewModel.selectSourceAccount(account)
            onNext()
        },
        onBack = onBack,
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun CreateTransferDebitContent(
    uiState: UiState<DebitStepData>,
    onRetry: () -> Unit = {},
    onAccountSelected: (BankAccountDetail) -> Unit = {},
    onBack: () -> Unit = {},
) {
    // Les labels sont maintenant fournis par BankAccountDetail.type?.name

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArkeoTopBar(title = "NOUVEAUX VIREMENT", onBack = onBack)
            UiStateHandler(
                uiState = uiState,
                onRetry = onRetry,
                loadingColor = ArkeoRed,
                errorColor = ArkeoRed,
            ) { data ->
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Compte à débiter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = ArkeoRed,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    items(data.accounts, key = { it.id ?: "" }) { account ->
                        DebitAccountCard(
                            account = account,
                            label = account.type?.name ?: "COMPTE",
                            onClick = { onAccountSelected(account) },
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

// ── Card compte à débiter ─────────────────────────────────────────────────────

@Composable
private fun DebitAccountCard(
    account: BankAccountDetail,
    label: String,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isPressed) Color(0xFFF0F0F0) else Color.White,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    color = ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    letterSpacing = 0.3.sp,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "N° ${maskIban(account.iban)}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatAmount(account.sold ?: 0.0),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextPrimary,
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun maskIban(iban: String?): String {
    val last2 = iban?.takeLast(2) ?: "XX"
    return "XXXXX$last2"
}

private fun formatAmount(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)

@Preview(showBackground = true, name = "State - Success")
@Composable
fun CreateTransferDebitScreenPreviewSuccess() {
    CreateTransferDebitContent(uiState = UiState.Success(DebitStepData(accounts = sampleTransferAccounts())))
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun CreateTransferDebitScreenPreviewLoading() {
    CreateTransferDebitContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun CreateTransferDebitScreenPreviewError() {
    CreateTransferDebitContent(uiState = UiState.Error(message = "Impossible de charger les comptes"))
}
