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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import defalt.featureAccount.viewModel.MenuViewModel
import defalt.ui.component.ArkeoTopBar
import defalt.ui.component.safeClick
import defalt.ui.utils.CustomColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: MenuViewModel = koinViewModel(),
) {
    MenuContent(
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToOffers = onNavigateToOffers,
        onLogout = {
            viewModel.logout()
            onLogout()
        },
        onBack = onBack,
    )
}

@Composable
private fun MenuContent(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomColor.BackgroundGray),
    ) {
        // En-tête
        ArkeoTopBar(title = "Menu", onBack = onBack)

        Spacer(modifier = Modifier.height(16.dp))

        // Liste des boutons menu
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            MenuRow(
                icon = Icons.Default.Person,
                label = "Mon profil",
                iconTint = CustomColor.ArkeoRed,
                onClick = onNavigateToProfile,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CustomColor.BackgroundGray,
            )
            MenuRow(
                icon = Icons.Default.LocalOffer,
                label = "Offres",
                iconTint = CustomColor.ArkeoRed,
                onClick = onNavigateToOffers,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CustomColor.BackgroundGray,
            )
            MenuRow(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                label = "Déconnexion",
                iconTint = Color(0xFFDA3F3F),
                labelColor = Color(0xFFDA3F3F),
                onClick = onLogout,
            )
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    label: String,
    iconTint: Color = CustomColor.ArkeoRed,
    labelColor: Color = CustomColor.TextPrimary,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = safeClick(onClick))
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconTint,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuContentPreview() {
    MenuContent()
}
