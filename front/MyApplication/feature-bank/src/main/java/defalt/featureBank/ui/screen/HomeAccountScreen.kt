package defalt.featureBank.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val ArkeoRed = Color(0xFFCC0000)
private val LightGray = Color(0xFFE5E5E5)
private val TextPrimary = Color(0xFF1A1A1A)
private val TextSecondary = Color(0xFF666666)

@Composable
fun HomeAccountScreen(
    onNavigateToAccounts: () -> Unit = {},
    onNavigateToTransfer: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bonjour X.XXXXXX",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextPrimary,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    MainAccountCard()
                }

                item {
                    SectionRowCard(
                        title = "TOUTE MON ÉPARGNE",
                        onClick = {},
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            HomeBottomNavBar(
                onNavigateToAccounts = onNavigateToAccounts,
                onNavigateToTransfer = onNavigateToTransfer,
            )
        }
    }
}

@Composable
private fun MainAccountCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "COMPTE CHÈQUES 1",
                color = ArkeoRed,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "XXXXX XXXXXXX",
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = formatAmount(478.27),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = TextPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ArkeoRed,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { },
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5F5F5),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "À venir :  ${formatAmount(0.0)}",
                        fontSize = 13.sp,
                        color = TextSecondary,
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                QuickAction(icon = Icons.Default.Receipt, label = "Relevés")
                QuickAction(icon = Icons.Default.AccountBalance, label = "RIB")
            }
        }
    }
}

@Composable
private fun QuickAction(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = TextPrimary,
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun SectionRowCard(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = title,
                    color = ArkeoRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ArkeoRed,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun HomeBottomNavBar(
    onNavigateToAccounts: () -> Unit,
    onNavigateToTransfer: () -> Unit,
) {
    data class NavItem(
        val label: String,
        val icon: ImageVector,
        val selected: Boolean = false,
        val onClick: () -> Unit = {},
    )

    val items = listOf(
        NavItem("Accueil", Icons.Default.Home, selected = true),
        NavItem("Comptes", Icons.AutoMirrored.Filled.List, onClick = onNavigateToAccounts),
        NavItem("Virement", Icons.Default.SwapHoriz, onClick = onNavigateToTransfer),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
            )
            .background(
                color = Color.White,
            )
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .clickable(onClick = item.onClick)
                    .padding(horizontal = 8.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (item.selected) ArkeoRed else TextSecondary,
                        modifier = Modifier.size(22.dp),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        color = if (item.selected) ArkeoRed else TextSecondary,
                        fontWeight = if (item.selected) FontWeight.Bold else FontWeight.Normal,
                    )
                }

                if (item.selected) {
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
                                    color = ArkeoRed,
                                    shape = RoundedCornerShape(
                                        bottomStart = 3.dp,
                                        bottomEnd = 3.dp,
                                    ),
                                ),
                        )
                    }
                }
            }
        }
    }
}

private fun formatAmount(value: Double): String =
    String.format(Locale.FRANCE, "%.2f €", value)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeAccountPreview() {
    HomeAccountScreen()
}
