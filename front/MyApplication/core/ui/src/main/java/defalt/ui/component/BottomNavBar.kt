package defalt.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes

/**
 * Composant réutilisable de Bottom Navigation (style HomeAccountScreen):
 * - supporte des items avec callbacks
 * - affiche une ombre et un indicateur en haut pour l'item sélectionné
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Routes,
    val selected: Boolean = false,
    var onClick: (() -> Unit)? = null,
)

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedRoute: Routes? = null,
    mapItems: Map<Routes, () -> Unit> = mapOf(),
) {
    val selectedColor = CustomColor.ArkeoRed
    val unselectedColor = CustomColor.TextSecondary
    val items = listOf(
        BottomNavItem("Accueil", Icons.Default.Home, route = Routes.Bank.Home),
        BottomNavItem("Comptes", Icons.AutoMirrored.Filled.List, route = Routes.Bank.ListAccount),
        BottomNavItem("Virements", Icons.Default.SwapHoriz, route = Routes.Operation),
    )
    items.forEach { items ->
        items.onClick = mapItems[items.route]
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp)
            .background(color = Color.White)
            .padding(start = 30.dp, end = 30.dp, bottom = 20.dp)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        items.forEach { item ->
            val isSelected = item.selected || (selectedRoute != null && item.route == selectedRoute)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, radius = 40.dp),
                        onClick = safeClick { item.onClick?.invoke() },
                    )
                    .padding(horizontal = 10.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp, bottom = 5.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) selectedColor else unselectedColor,
                        modifier = Modifier.size(28.dp),
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        color = if (isSelected) selectedColor else unselectedColor,
                    )
                }

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .matchParentSize(),
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(
                                    color = selectedColor,
                                    shape = RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp),
                                ),
                        )
                    }
                }
            }
        }
    }
}

private fun defaultItems(): List<BottomNavItem> = listOf(
    BottomNavItem("Accueil", Icons.Default.Home, route = Routes.Bank.Home),
    BottomNavItem("Comptes", Icons.AutoMirrored.Filled.List, route = Routes.Bank.ListAccount),
    BottomNavItem("Virements", Icons.Default.SwapHoriz, route = Routes.Operation),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 120)
@Composable
private fun BottomNavBarPreview() {
    BottomNavBar(selectedRoute = Routes.Bank.ListAccount)
}
