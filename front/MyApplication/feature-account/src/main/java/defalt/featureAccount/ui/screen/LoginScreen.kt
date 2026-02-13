package defalt.featureAccount.ui.screen

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import defalt.core.ui.CustomColor
import defalt.core.ui.component.ArkeoButton
import defalt.core.ui.component.ArkeoInput

@Composable
fun LoginScreen(onBackToHome: () -> Unit) {
    // État local pour les champs de texte
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(CustomColor.BackgroundGray)) {
        // Header Rouge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CustomColor.ArkeoRed)
                .padding(top = 48.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("ESPACE CLIENT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Carte Formulaire Connexion
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text("IDENTIFICATION", color = CustomColor.ArkeoRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                ArkeoInput(
                    value = login,
                    onValueChange = { login = it },
                    label = "Identifiant",
                    icon = Icons.Outlined.Person,
                )

                ArkeoInput(
                    value = password,
                    onValueChange = { password = it },
                    label = "Mot de passe",
                    icon = Icons.Outlined.Lock,
                    isPassword = true,
                )

                // Lien mot de passe oublié
                Text(
                    text = "Mot de passe oublié ?",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.End),
                )

                Spacer(modifier = Modifier.height(8.dp))

                ArkeoButton(text = "ACCÉDER À MES COMPTES", onClick = { /* TODO: Logique de connexion */ })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onBackToHome,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp),
        ) {
            Text("Retour à l'accueil", color = CustomColor.ArkeoRed)
        }
    }
}
