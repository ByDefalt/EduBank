package defalt.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor

/**
 * Ligne label : valeur utilisée dans les cards de détail (infos, opérations…).
 */
@Composable
fun ArkeoLabelValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label :",
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.4f),
            color = CustomColor.TextPrimary,
        )
        Text(
            text = value,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.6f),
            color = CustomColor.TextPrimary,
        )
    }
}
