package defalt.featureOperation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import defalt.ui.component.ArkeoButton
import defalt.ui.component.ArkeoInput
import defalt.ui.component.BottomNavBar
import defalt.ui.component.safeClick
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import java.text.Normalizer

// Écran "Mes bénéficiaires" aligné au style des autres écrans (header comme AccountDetailsScreen, fond gris, cards arrondies)

private val ArkeoRed = CustomColor.ArkeoRed
private val TextPrimary = CustomColor.TextPrimary

data class Beneficiary(
    val id: String,
    val name: String,
    val accountNumber: String,
    val bankName: String,
)

// utilitaire : normalise la première lettre -> supprime accents et renvoie A..Z ou '#'
private fun initialOf(name: String): Char {
    if (name.isBlank()) return '#'
    val normalized = Normalizer.normalize(name.trim(), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}"), "")
    val first = normalized.firstOrNull()?.uppercaseChar() ?: '#'
    return if (first in 'A'..'Z') first else '#'
}

@Composable
fun BeneficiariesScreen(
    beneficiaries: List<Beneficiary> = defaultData(),
    query: String = "",
    onQueryChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onItemClick: (Beneficiary) -> Unit = {},
    onBack: () -> Unit = {},
    onAddBeneficiary: () -> Unit = {},
    onNavigateToHomeBank: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    // Filtre, tri et groupement
    val filtered = remember(beneficiaries, query) {
        beneficiaries
            .filter { it.name.contains(query, ignoreCase = true) }
            .sortedBy { it.name.lowercase() }
    }

    val grouped = remember(filtered) {
        filtered.groupBy { initialOf(it.name) }
            .toSortedMap()
    }

    val safeNavigateBack = safeClick(onBack)
    val safeNavigateHome = safeClick(onNavigateToHomeBank)
    val safeNavigateAccounts = safeClick(onNavigateToAccounts)
    val safeNavigateTransfer = safeClick(onNavigateToTransfer)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header identique à AccountDetailsScreen
            BeneficiariesHeader(onNavigateBack = safeNavigateBack)

            // Zone contenu
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            ) {
            item {
                // Search bar (utilise le composant partagé ArkeoInput)
                ArkeoInput(
                    value = query,
                    onValueChange = onQueryChanged,
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

                    items(list, key = { it.id }) { b ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(b.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Compte: ${b.accountNumber}", fontSize = 12.sp, color = CustomColor.TextSecondary)
                                    Text(b.bankName, fontSize = 12.sp, color = CustomColor.TextSecondary)
                                }

                                // Optionnel: bouton d'action ou chevron
                            }
                        }
                    }
                }
                }
            }  // fin LazyColumn

            BottomNavBar(
                selectedRoute = Routes.Operation,
                mapItems = mapOf(
                    Routes.Bank.Home to safeNavigateHome,
                    Routes.Bank.ListAccount to safeNavigateAccounts,
                    Routes.Operation to safeNavigateTransfer,
                ),
            )
        }  // fin Column

        // Bouton flottant au-dessus de la BottomNavBar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Transparent)
                .navigationBarsPadding()
                .padding(bottom = 90.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            ArkeoButton(
                text = "Ajouter un bénéficiaire",
                onClick = onAddBeneficiary,
            )
        }
    }  // fin Box principal
}

@Composable
private fun BeneficiariesHeader(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = ArkeoRed,
            )
        }
        Text(
            text = "MES BÉNÉFICIAIRES",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(48.dp))
    }
}

fun defaultData() : List<Beneficiary> {
    return listOf(
        Beneficiary("1", "Alice Dupont", "FR76 1234 5678 9012", "Banque A"),
        Beneficiary("2", "Amine Saïd", "FR76 2222 3333 4444", "Banque B"),
        Beneficiary("3", "Bruno Martin", "FR76 5555 6666 7777", "Banque C"),
        Beneficiary("4", "Claire Noël", "FR76 8888 9999 0000", "Banque A"),
        Beneficiary("5", "David Petit", "FR76 1111 2222 3333", "Banque B"),
        Beneficiary("6", "Élodie Faure", "FR76 4444 5555 6666", "Banque C"),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBeneficiariesScreen() {
    var query by remember { mutableStateOf("") }
    BeneficiariesScreen(
        beneficiaries = defaultData(),
        query = query,
        onQueryChanged = { query = it },
    )
}
