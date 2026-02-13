package defalt.core.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.ui.utils.CustomColor
import defalt.core.ui.component.ArkeoButton

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToOffer: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // En-tête Logo
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("ARKEO BANQUE", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("ENTREPRISES & INSTITUTIONNELS", fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }

        // Zone Image "Pont"
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CustomColor.BridgeTeal),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DE NOUVEAUX\nLIENS POUR", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Surface(color = CustomColor.ArkeoRed, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(" ÉCHANGER ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                Text("INNOVER DEMAIN", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }

        // Zone Actions
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            ArkeoButton(text = "Se connecter", onClick = onNavigateToLogin)

            Spacer(modifier = Modifier.height(16.dp))

            // Nouveau bouton vers l'écran Offres
            ArkeoButton(text = "Voir les offres", onClick = onNavigateToOffer)

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Ouvrir un compte", color = CustomColor.ArkeoRed, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToLogin = {}, onNavigateToRegister = {})
}

