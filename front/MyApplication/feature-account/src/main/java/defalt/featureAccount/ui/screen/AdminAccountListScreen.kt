package defalt.featureAccount.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.account.Account
import defalt.domain.entity.account.AccountStateEnum
import defalt.featureAccount.viewModel.AdminAccountListViewModel
import defalt.ui.component.ArkeoStatusBadge
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminAccountListScreen(
    onBack: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onRefreshConsumed: () -> Unit = {},
    shouldRefresh: Boolean = false,
    viewModel: AdminAccountListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.retry()
            onRefreshConsumed()
        }
    }

    AdminAccountListContent(uiState = uiState, onRetry = viewModel::retry, onBack = onBack, onItemClick = onItemClick)
}

@Composable
internal fun AdminAccountListContent(
    uiState: UiState<List<Account>>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        ArkeoTopBar(title = "COMPTES UTILISATEURS", onBack = onBack)

        UiStateHandler(
            uiState = uiState,
            modifier = Modifier.weight(1f),
            onRetry = onRetry,
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { accounts ->
            LazyColumn(modifier = Modifier.padding(12.dp)) {
                items(accounts) { account ->
                    AccountAdminCard(account = account, onClick = { onItemClick(account.id ?: "") })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AccountAdminCard(account: Account, onClick: () -> Unit) {
    val isActive = account.state == AccountStateEnum.ACTIVE
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
                    text = "Compte #${account.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = CustomColor.TextPrimary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ArkeoStatusBadge(
                    label = account.state?.value ?: "?",
                    isActive = isActive,
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CustomColor.ArkeoRed)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AdminAccountListContent(
        uiState = UiState.Success(
            listOf(
                Account(id = "acc-0001", state = AccountStateEnum.ACTIVE, roleId = 1),
                Account(id = "acc-0002", state = AccountStateEnum.INACTIVE, roleId = 2),
            ),
        ),
    )
}
