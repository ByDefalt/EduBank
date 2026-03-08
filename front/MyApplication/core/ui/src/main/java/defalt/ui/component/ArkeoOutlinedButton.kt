package defalt.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import defalt.ui.utils.CustomColor

/**
 * Bouton outline rouge pour les actions destructives (supprimer, désactiver, annuler…).
 */
@Composable
fun ArkeoOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CustomColor.ArkeoRed),
        border = BorderStroke(1.5.dp, CustomColor.ArkeoRed),
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, color = CustomColor.ArkeoRed)
    }
}

