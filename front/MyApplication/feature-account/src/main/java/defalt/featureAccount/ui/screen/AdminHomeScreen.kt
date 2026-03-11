package defalt.featureAccount.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.featureAccount.viewModel.AdminHomeViewModel
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdminHomeScreen(
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToBankAccounts: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onNavigateToOperations: () -> Unit = {},
    onLogout: () -> Unit = {},
    adminHomeViewModel: AdminHomeViewModel = koinViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text("ADMINISTRATION", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = CustomColor.ArkeoRed)
        }

        Spacer(modifier = Modifier.height(8.dp))

        AdminMenuCard(icon = Icons.Default.People, title = "Comptes utilisateurs", subtitle = "Gérer les comptes et leur état", onClick = onNavigateToAccounts)
        AdminMenuCard(icon = Icons.Default.AccountBalance, title = "Comptes bancaires", subtitle = "Gérer les comptes bancaires", onClick = onNavigateToBankAccounts)
        AdminMenuCard(icon = Icons.Default.CardGiftcard, title = "Offres", subtitle = "Créer et gérer les offres", onClick = onNavigateToOffers)
        AdminMenuCard(icon = Icons.Default.SwapHoriz, title = "Opérations", subtitle = "Consulter et gérer les opérations", onClick = onNavigateToOperations)
        AdminMenuCard(
            icon = Icons.Default.ChevronRight,
            title = "Déconnexion",
            subtitle = "Se déconnecter de l'application",
            onClick = { adminHomeViewModel.logout(); onLogout.invoke() },
        )
    }
}

@Composable
private fun AdminMenuCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(icon, contentDescription = null, tint = CustomColor.ArkeoRed, modifier = Modifier.size(32.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = CustomColor.TextPrimary)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = CustomColor.ArkeoRed)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() { AdminHomeScreen() }
