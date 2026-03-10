package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.bank.BankAccount
import defalt.featureBank.viewModel.AdminBankListViewModel
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminBankListScreen(
    onBack: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onCreateClick: () -> Unit = {},
    viewModel: AdminBankListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdminBankListContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onBack = onBack,
        onItemClick = onItemClick,
        onCreateClick = onCreateClick,
    )
}

@Composable
internal fun AdminBankListContent(
    uiState: UiState<List<BankAccount>>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onCreateClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArkeoTopBar(title = "COMPTES BANCAIRES", onBack = onBack)

            // ── Liste ──────────────────────────────────────────────────────
            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = CustomColor.ArkeoRed,
                errorColor = CustomColor.ArkeoRed,
            ) { accounts ->
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(accounts) { account ->
                        BankAdminCard(account = account, onClick = { onItemClick(account.id ?: "") })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // ── FAB Créer ──────────────────────────────────────────────────────
        FloatingActionButton(
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = CustomColor.ArkeoRed,
        ) {
            Icon(Icons.Default.Add, contentDescription = "Créer un compte bancaire", tint = Color.White)
        }
    }
}

@Composable
private fun BankAdminCard(account: BankAccount, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = account.iban ?: account.id ?: "-",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CustomColor.TextPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Solde : %.2f €".format(account.sold ?: 0.0),
                    fontSize = 13.sp,
                    color = if ((account.sold ?: 0.0) >= 0) Color(0xFF4CAF50) else Color(0xFFF44336),
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CustomColor.ArkeoRed)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AdminBankListContent(
        uiState = UiState.Success(
            listOf(
                BankAccount(id = "bank-0001", iban = "FR76 3000 6000 0112 3456", sold = 1250.75, typeId = 1, parameterId = 1),
                BankAccount(id = "bank-0002", iban = "FR76 3000 6000 0198 7654", sold = -120.50, typeId = 2, parameterId = 2),
            ),
        ),
    )
}
