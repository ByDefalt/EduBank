package defalt.feature_account.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.ui.CustomColor
import defalt.core.ui.component.ArkeoButton
import defalt.core.ui.component.ArkeoInput

@Composable
fun RegisterScreen(onBackToHome: () -> Unit) {
    // État local (optionnel pour la démo)
    var raisonSociale by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        // Header Rouge
        Box(
            modifier = Modifier.fillMaxWidth().background(CustomColor.ArkeoRed).padding(top = 48.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("OUVERTURE DE COMPTE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Carte Formulaire Inscription
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("VOS COORDONNÉES", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                ArkeoInput(raisonSociale, { raisonSociale = it }, "Raison Sociale", icon = Icons.Outlined.Person)
                ArkeoInput(email, { email = it }, "Email / Identifiant", keyboardType = KeyboardType.Email)
                ArkeoInput(password, { password = it }, "Mot de passe", icon = Icons.Outlined.Lock, isPassword = true)

                Spacer(modifier = Modifier.height(8.dp))
                ArkeoButton(text = "VALIDER LA DEMANDE", onClick = { })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onBackToHome,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
        ) {
            Text("Annuler et retour", color = CustomColor.ArkeoRed)
        }
    }
}
