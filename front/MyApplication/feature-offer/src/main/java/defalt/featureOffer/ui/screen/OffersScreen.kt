package defalt.featureOffer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import defalt.domain.entity.offer.Offer
import defalt.featureOffer.viewModel.OffersViewModel
import defalt.ui.component.ArkeoButton
import defalt.ui.component.UiStateHandler
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import java.time.LocalDate
import org.koin.androidx.compose.koinViewModel

// ── Composable stateful (prod) ───────────────────────────────────────────────
@Composable
fun OffersScreen(
    onBack: () -> Unit,
    viewModel: OffersViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OffersContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onBack = onBack,
    )
}

// ── Composable stateless (testable / previewable) ────────────────────────────
@Composable
internal fun OffersContent(
    uiState: UiState<List<Offer>>,
    onRetry: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.ArkeoRed)
                .padding(top = 48.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "OFFRES",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }

        UiStateHandler(
            uiState = uiState,
            onRetry = onRetry,
            loadingColor = CustomColor.ArkeoRed,
            errorColor = CustomColor.ArkeoRed,
        ) { offers ->
            // Liste d'offres
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp),
            ) {
                items(offers) { offer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            // Image placeholder si picturePath present sinon carré coloré
                            if (offer.picturePath != null) {
                                Image(
                                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                    contentDescription = "offer image",
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(CustomColor.BridgeTeal),
                                )
                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(offer.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(offer.description, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text("Du ${offer.startDate} au ${offer.endDate}", fontSize = 11.sp)
                                    Text(
                                        offer.state.name,
                                        color = CustomColor.ArkeoRed,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    ArkeoButton(text = "Retour", onClick = onBack)
                }
            }
        } // fin UiStateHandler
    }
}

private fun sampleOffers(): List<Offer> = listOf(
    Offer(
        id = 1,
        title = "Offre de bienvenue",
        description = "Taux préférentiel pour les nouveaux clients",
        state = Offer.State.ACTIVE,
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusMonths(1),
        picturePath = null,
    ),
    Offer(
        id = 2,
        title = "Crédit pro",
        description = "Financement à taux avantageux pour les entreprises",
        state = Offer.State.ACTIVE,
        startDate = LocalDate.now().minusMonths(2),
        endDate = LocalDate.now().plusMonths(2),
        picturePath = null,
    ),
)

@Preview(showBackground = true, name = "State - Success")
@Composable
fun OffersScreenPreviewSuccess() {
    OffersContent(uiState = UiState.Success(sampleOffers()))
}

@Preview(showBackground = true, name = "State - Loading")
@Composable
fun OffersScreenPreviewLoading() {
    OffersContent(uiState = UiState.Loading)
}

@Preview(showBackground = true, name = "State - Error")
@Composable
fun OffersScreenPreviewError() {
    OffersContent(uiState = UiState.Error(message = "Impossible de charger les offres"))
}
