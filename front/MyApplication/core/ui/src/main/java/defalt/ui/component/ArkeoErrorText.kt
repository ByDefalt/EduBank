package defalt.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor

/**
 * Texte d'erreur rouge affiché dans les formulaires (Login, Register…).
 */
@Composable
fun ArkeoErrorText(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        color = CustomColor.ArkeoRed,
        fontSize = 13.sp,
        modifier = modifier,
    )
}
