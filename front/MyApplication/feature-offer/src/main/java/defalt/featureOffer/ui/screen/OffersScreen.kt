package defalt.featureOffer.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.ui.component.ArkeoButton
import defalt.core.ui.utils.CustomColor
import defalt.core.api.offer.entity.OfferEntity
import java.time.LocalDate

@Composable
fun OffersScreen(onBack: () -> Unit) {
    // Pour la démo on utilise une liste statique de Offers
    val offers = remember { sampleOffers() }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(CustomColor.BackgroundGray)) {
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
                fontSize = 20.sp
            )
        }

        // Liste d'offres
        LazyColumn(modifier = Modifier
            .padding(16.dp)) {
            items(offers) { offer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        // Image placeholder si picturePath present sinon carré coloré
                        if (offer.picturePath != null) {
                            // Pour l'instant on utilise un drawable par défaut si disponible
                            Image(
                                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                                contentDescription = "offer image",
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(modifier = Modifier
                                .size(64.dp)
                                .background(CustomColor.BridgeTeal))
                        }

                        Spacer(modifier = Modifier.padding(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(offer.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(offer.description, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text("Du ${offer.startDate} au ${offer.endDate}", fontSize = 11.sp)
                                Text(offer.state.name, color = CustomColor.ArkeoRed, fontWeight = FontWeight.SemiBold)
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
    }
}

private fun sampleOffers(): List<OfferEntity> = listOf(
    OfferEntity(
        id = 1,
        title = "Offre de bienvenue",
        description = "Taux préférentiel pour les nouveaux clients",
        state = OfferEntity.State.ACTIVE,
        startDate = LocalDate.now(),
        endDate = LocalDate.now().plusMonths(1),
        picturePath = null,
    ),
    OfferEntity(
        id = 2,
        title = "Crédit pro",
        description = "Financement à taux avantageux pour les entreprises",
        state = OfferEntity.State.ACTIVE,
        startDate = LocalDate.now().minusMonths(2),
        endDate = LocalDate.now().plusMonths(2),
        picturePath = null,
    ),
)

@Preview(showBackground = true)
@Composable
fun OffersScreenPreview() {
    OffersScreen(onBack = {})
}

