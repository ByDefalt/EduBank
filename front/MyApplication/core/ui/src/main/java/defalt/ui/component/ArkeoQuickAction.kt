package defalt.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor

/**
 * Bouton d'action rapide avec icône + libellé (ex : Relevés, RIB).
 * Utilisé dans HomeAccountScreen et AccountDetailsScreen.
 */
@Composable
fun ArkeoQuickAction(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = CustomColor.TextPrimary,
            modifier = Modifier.size(28.dp),
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = label, fontSize = 11.sp, color = CustomColor.TextSecondary)
    }
}
