package defalt.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.ui.utils.CustomColor

// ── 1. TopBar ────────────────────────────────────────────────────────────────

/**
 * Barre de titre commune à tous les écrans admin.
 * Affiche une flèche retour à gauche et un titre centré.
 */
@Composable
fun ArkeoTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = CustomColor.ArkeoRed,
            )
        }
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = CustomColor.TextPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
        )
        // Espace fantôme pour centrer le titre (même largeur que l'IconButton)
        Spacer(modifier = Modifier.size(48.dp))
    }
}

// ── 2. Card section ──────────────────────────────────────────────────────────

/**
 * Card blanche avec un titre en rouge en en-tête, utilisée dans les écrans de détail.
 */
@Composable
fun ArkeoCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                color = CustomColor.ArkeoRed,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
            content()
        }
    }
}

// ── 3. LabelValue ────────────────────────────────────────────────────────────

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

// ── 4. OutlinedButton destructif ─────────────────────────────────────────────

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

// ── 5. StatusBadge ───────────────────────────────────────────────────────────

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

// ── 6. QuickAction ───────────────────────────────────────────────────────────

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

// ── 7. ErrorText ─────────────────────────────────────────────────────────────

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
