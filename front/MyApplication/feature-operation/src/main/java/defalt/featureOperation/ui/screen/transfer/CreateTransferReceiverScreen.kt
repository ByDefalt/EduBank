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
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.bank.BankAccount
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.viewModel.CreateTransferViewModel
import defalt.featureOperation.viewModel.ReceiverStepData
import defalt.featureOperation.viewModel.sampleBeneficiaries2
import defalt.featureOperation.viewModel.sampleTransferAccounts
import defalt.ui.component.UiStateHandler
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.util.Locale
import org.koin.androidx.compose.koinViewModel

private val ArkeoRed = CustomColor.ArkeoRed
private val TextPrimary = CustomColor.TextPrimary
private val TextSecondary = CustomColor.TextSecondary

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun CreateTransferReceiverScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    viewModel: CreateTransferViewModel = koinViewModel(),
) {
    val uiState by viewModel.receiverUiState.collectAsStateWithLifecycle()

    val safeBack = safeClick(onBack)

    CreateTransferReceiverContent(
        uiState = uiState,
        onRetry = viewModel::retryReceiver,
        onBack = safeBack,
        onAccountSelected = { account ->
            viewModel.selectReceiverAccount(account)
            onNext()
        },
        onBeneficiarySelected = { beneficiary ->
            viewModel.selectReceiverBeneficiary(beneficiary)
            onNext()
        },
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun CreateTransferReceiverContent(
    uiState: UiState<ReceiverStepData>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onAccountSelected: (BankAccount) -> Unit = {},
    onBeneficiarySelected: (Beneficiary) -> Unit = {},
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val typeNames = mapOf(
        1 to "COMPTE CHÈQUES",
        2 to "COMPTE ÉPARGNE",
        3 to "COMPTE PROFESSIONNEL",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // ── Header avec retour arrière ────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = ArkeoRed,
                    )
                }
                Text(
                    text = "BÉNÉFICIAIRE DU VIREMENT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(48.dp))
            }

            // Les tabs restent visibles même en loading/erreur
            SecondaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = ArkeoRed,
                indicator = {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(selectedTab),
                        color = ArkeoRed,
                    )
                },
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Compte",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) ArkeoRed else TextSecondary,
                        )
                    },
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Bénéficiaire",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) ArkeoRed else TextSecondary,
                        )
                    },
                )
            }

            UiStateHandler(
                uiState = uiState,
                onRetry = onRetry,
                loadingColor = ArkeoRed,
                errorColor = ArkeoRed,
            ) { data ->
                // ── Contenu selon l'onglet ────────────────────────────────────
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    if (selectedTab == 0) {
                        // ── Onglet Compte perso ──────────────────────────────
                        item {
                            Text(
                                text = "Mes comptes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArkeoRed,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        if (data.filteredAccounts.isEmpty()) {
                            item {
                                EmptyListMessage(message = "Aucun autre compte disponible pour ce virement.")
                            }
                        } else {
                            items(data.filteredAccounts, key = { it.id ?: "" }) { account ->
                                ReceiverAccountCard(
                                    account = account,
                                    label = typeNames[account.typeId] ?: "COMPTE",
                                    onClick = { onAccountSelected(account) },
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    } else {
                        // ── Onglet Bénéficiaire ──────────────────────────────
                        item {
                            Text(
                                text = "Mes bénéficiaires",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = ArkeoRed,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        if (data.beneficiaries.isEmpty()) {
                            item {
                                EmptyListMessage(message = "Aucun bénéficiaire enregistré.\nAjoutez-en un depuis la liste des bénéficiaires.")
                            }
                        } else {
                            items(data.beneficiaries, key = { it.id ?: it.name }) { beneficiary ->
                                ReceiverBeneficiaryCard(
                                    beneficiary = beneficiary,
                                    onClick = { onBeneficiarySelected(beneficiary) },
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            } // fin UiStateHandler
        }
    }
}

// ── Card compte perso ─────────────────────────────────────────────────────────

@Composable
private fun ReceiverAccountCard(
    account: BankAccount,
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
                    text = "N° ${maskIbanReceiver(account.iban)}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatAmountReceiver(account.sold ?: 0.0),
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

// ── Card bénéficiaire ─────────────────────────────────────────────────────────

@Composable
private fun ReceiverBeneficiaryCard(
    beneficiary: Beneficiary,
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
                    text = beneficiary.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "IBAN : ${beneficiary.ibanTarget}",
                    color = TextSecondary,
                    fontSize = 12.sp,
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

// ── Message liste vide ────────────────────────────────────────────────────────

@Composable
private fun EmptyListMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(36.dp),
        )
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun maskIbanReceiver(iban: String?): String {
    val last2 = iban?.takeLast(2) ?: "XX"
    return "XXXXX$last2"
}

private fun formatAmountReceiver(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)

@Preview(showBackground = true, name = "State - Success")
@Composable
fun CreateTransferReceiverScreenPreviewSuccess() {
    CreateTransferReceiverContent(
        uiState = UiState.Success(
            ReceiverStepData(
                accounts = sampleTransferAccounts(),
                beneficiaries = sampleBeneficiaries2(),
            ),
        ),
    )
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun CreateTransferReceiverScreenPreviewLoading() {
    CreateTransferReceiverContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun CreateTransferReceiverScreenPreviewError() {
    CreateTransferReceiverContent(uiState = UiState.Error(message = "Erreur de chargement"))
}
