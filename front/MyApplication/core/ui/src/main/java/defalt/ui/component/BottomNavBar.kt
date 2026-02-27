package defalt.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor

/**
 * Composant réutilisable de Bottom Navigation (style HomeAccountScreen):
 * - supporte des items avec callbacks
 * - affiche une ombre et un indicateur en haut pour l'item sélectionné
 */
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String = label,
    val selected: Boolean = false,
    val onClick: (() -> Unit)? = null,
)

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selectedRoute: String = "",
    items: List<BottomNavItem> = defaultItems(),
    onNavigate: (String) -> Unit = {},
) {
    val selectedColor = CustomColor.ArkeoRed
    val unselectedColor = CustomColor.TextSecondary

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp)
            .background(color = Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        items.forEach { item ->
            val isSelected = item.selected || item.route == selectedRoute
            Box(
                modifier = Modifier
                    .clickable {
                        item.onClick?.invoke() ?: onNavigate(item.route)
                    }
                    .padding(horizontal = 8.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) selectedColor else unselectedColor,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
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
    BottomNavItem("Accueil", Icons.Default.Home, selected = true),
    BottomNavItem("Comptes", Icons.AutoMirrored.Filled.List),
    BottomNavItem("Virements", Icons.Default.SwapHoriz),
)

@Preview(showBackground = true, widthDp = 390, heightDp = 120)
@Composable
private fun BottomNavBarPreview() {
    BottomNavBar(selectedRoute = "Comptes")
}
