package defalt.featureOperation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.operation.Operation
import defalt.featureOperation.viewModel.AdminOperationListViewModel
import defalt.ui.component.ArkeoStatusBadge
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.utils.CustomColor
import java.time.format.DateTimeFormatter
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminOperationListScreen(
    onBack: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onRefreshConsumed: () -> Unit = {},
    shouldRefresh: Boolean = false,
    viewModel: AdminOperationListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(shouldRefresh) {
        if (shouldRefresh) {
            viewModel.retry()
            onRefreshConsumed()
        }
    }
    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        ArkeoTopBar(title = "OPÉRATIONS", onBack = onBack)

        UiStateHandler(
            uiState = uiState,
            onRetry = viewModel::retry,
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { operations ->
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(operations) { op ->
                    OperationRow(operation = op, onClick = { onItemClick(op.id) })
                }
            }
        }
    }
}

@Composable
private fun OperationRow(operation: Operation, onClick: () -> Unit) {
    val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(operation.label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = CustomColor.TextPrimary)
                Text("%.2f €".format(operation.amount), fontSize = 12.sp, color = CustomColor.TextSecondary)
                Text(operation.date.format(fmt), fontSize = 11.sp, color = CustomColor.TextSecondary)
                ArkeoStatusBadge(label = operation.state.value, isActive = operation.state.value == "PENDING" || operation.state.value == "COMPLETED")
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CustomColor.ArkeoRed, modifier = Modifier.size(20.dp))
        }
    }
}
