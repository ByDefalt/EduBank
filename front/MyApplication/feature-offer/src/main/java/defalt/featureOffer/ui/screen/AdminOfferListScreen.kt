package defalt.featureOffer.ui.screen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import defalt.domain.entity.offer.Offer
import defalt.featureOffer.viewModel.OffersViewModel
import defalt.ui.component.ArkeoStatusBadge
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.time.LocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminOfferListScreen(
    onBack: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onCreateClick: () -> Unit = {},
    viewModel: OffersViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AdminOfferListContent(uiState = uiState, onRetry = viewModel::retry, onBack = onBack, onItemClick = onItemClick, onCreateClick = onCreateClick)
}

@Composable
internal fun AdminOfferListContent(
    uiState: UiState<List<Offer>>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
    onItemClick: (Int) -> Unit = {},
    onCreateClick: () -> Unit = {},
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick, containerColor = CustomColor.ArkeoRed, shape = CircleShape) {
                Icon(Icons.Default.Add, contentDescription = "Créer", tint = Color.White)
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray).padding(padding)) {
            ArkeoTopBar(title = "OFFRES", onBack = onBack)

            UiStateHandler(
                uiState = uiState,
                modifier = Modifier.weight(1f),
                onRetry = onRetry,
                loadingColor = CustomColor.ArkeoRed,
                errorColor = CustomColor.ArkeoRed,
            ) { offers ->
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(offers) { offer ->
                        OfferAdminCard(offer = offer, onClick = { onItemClick(offer.id) })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferAdminCard(offer: Offer, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(offer.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = CustomColor.TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                ArkeoStatusBadge(label = offer.state.value, isActive = offer.state == Offer.State.ACTIVE)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CustomColor.ArkeoRed)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AdminOfferListContent(
        uiState = UiState.Success(
            listOf(
                Offer(1, "Offre bienvenue", "Desc", Offer.State.ACTIVE, LocalDate.now(), LocalDate.now().plusMonths(1)),
                Offer(2, "Prêt pro", "Desc", Offer.State.INACTIVE, LocalDate.now(), LocalDate.now().plusMonths(3)),
            ),
        ),
    )
}
