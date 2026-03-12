package defalt.featureOperation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.operation.Beneficiary
import defalt.featureOperation.viewModel.BeneficiariesViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoInput
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.BottomNavBar
import defalt.ui.component.UiStateHandler
import defalt.ui.component.safeClick
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import java.text.Normalizer
import org.koin.androidx.compose.koinViewModel

private val ArkeoRed = CustomColor.ArkeoRed
private val TextPrimary = CustomColor.TextPrimary


private fun initialOf(name: String): Char {
    if (name.isBlank()) return '#'
    val normalized = Normalizer.normalize(name.trim(), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "")
    val first = normalized.firstOrNull()?.uppercaseChar() ?: '#'
    return if (first in 'A'..'Z') first else '#'
}


@Composable
fun BeneficiariesScreen(
    onItemClick: (Beneficiary) -> Unit = {},
    onBack: () -> Unit = {},
    onAddBeneficiary: () -> Unit = {},
    shouldRefresh: Boolean = false,
    onRefreshConsumed: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
    viewModel: BeneficiariesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.retry()
            onRefreshConsumed()
        }
    }

    BeneficiariesContent(
        uiState = uiState,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        onRetry = viewModel::retry,
        onItemClick = onItemClick,
        onBack = onBack,
        onAddBeneficiary = onAddBeneficiary,
        onNavigateToHomeBank = onNavigateToHomeBank,
        onNavigateToAccounts = onNavigateToAccounts,
        onNavigateToTransfer = onNavigateToTransfer,
    )
}


@Composable
internal fun BeneficiariesContent(
    uiState: UiState<List<Beneficiary>>,
    onRetry: () -> Unit = {},
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onItemClick: (Beneficiary) -> Unit = {},
    onBack: () -> Unit = {},
    onAddBeneficiary: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    val safeNavigateBack = safeClick(onBack)
    val safeNavigateHome = safeClick(onNavigateToHomeBank)
    val safeNavigateAccounts = safeClick(onNavigateToAccounts)
    val safeNavigateTransfer = safeClick(onNavigateToTransfer)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArkeoTopBar(title = "MES BÉNÉFICIAIRES", onBack = safeNavigateBack)

            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = ArkeoRed,
                errorColor = ArkeoRed,
            ) { beneficiaries ->
                val filtered = remember(beneficiaries, query) {
                    beneficiaries
                        .filter { it.name.contains(query, ignoreCase = true) }
                        .sortedBy { it.name.lowercase() }
                }

                val grouped = remember(filtered) {
                    filtered.groupBy { initialOf(it.name) }
                        .toSortedMap()
                }


                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                    ) {
                        item {
                            ArkeoInput(
                                value = query,
                                onValueChange = onQueryChange,
                                label = "Rechercher un bénéficiaire",
                                icon = Icons.Default.Search,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (grouped.isEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                ) {
                                    Column(modifier = Modifier.padding(24.dp)) {
                                        Text("Aucun bénéficiaire", color = TextPrimary)
                                    }
                                }
                            }
                        } else {
                            grouped.forEach { (letter, list) ->
                                item {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp, bottom = 4.dp)
                                            .size(32.dp)
                                            .background(
                                                color = Color(0xFF3A3A3A),
                                                shape = RoundedCornerShape(
                                                    topStart = 10.dp,
                                                    topEnd = 0.dp,
                                                    bottomStart = 0.dp,
                                                    bottomEnd = 10.dp,
                                                ),
                                            ),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = letter.toString(),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White,
                                        )
                                    }
                                }

                                items(list, key = { it.id ?: it.name }) { b ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        onClick = { onItemClick(b) },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(b.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text("IBAN : ${b.ibanTarget}", fontSize = 12.sp, color = CustomColor.TextSecondary)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .navigationBarsPadding()
                            .padding(bottom = 8.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        ArkeoButton(
                            text = "Ajouter un bénéficiaire",
                            onClick = onAddBeneficiary,
                        )
                    }
                }
            }

            BottomNavBar(
                selectedRoute = Routes.Operation,
                mapItems = mapOf(
                    Routes.Bank.Home to safeNavigateHome,
                    Routes.Bank.ListAccount to safeNavigateAccounts,
                    Routes.Operation to safeNavigateTransfer,
                ),
            )
        }
    }
}

private fun defaultData(): List<Beneficiary> = listOf(
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1234 5678 9012", name = "Alice Dupont", id = 1),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 2222 3333 4444", name = "Amine Saïd", id = 2),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 5555 6666 7777", name = "Bruno Martin", id = 3),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 8888 9999 0000", name = "Claire Noël", id = 4),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 1111 2222 3333", name = "David Petit", id = 5),
    Beneficiary(accountSourceId = "1", ibanTarget = "FR76 4444 5555 6666", name = "Élodie Faure", id = 6),
)

@Preview(showBackground = true, name = "State - Success")
@Composable
fun BeneficiariesPreviewSuccess() {
    BeneficiariesContent(uiState = UiState.Success(defaultData()))
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun BeneficiariesPreviewLoading() {
    BeneficiariesContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun BeneficiariesPreviewError() {
    BeneficiariesContent(uiState = UiState.Error(message = "Impossible de charger les bénéficiaires"))
}
