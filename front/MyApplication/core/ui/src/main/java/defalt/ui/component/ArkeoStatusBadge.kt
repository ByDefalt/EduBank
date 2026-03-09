package defalt.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Badge coloré indiquant un état actif (vert) ou inactif/bloqué (gris).
 */
@Composable
fun ArkeoStatusBadge(
    label: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = if (isActive) Color(0xFF4CAF50) else Color(0xFFBDBDBD),
                shape = RoundedCornerShape(50),
            )
            .padding(horizontal = 10.dp, vertical = 2.dp),
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}

