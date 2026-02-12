package defalt.eduBank

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
// --- IMPORTS ICÔNES (C'est OK d'utiliser material.icons avec material3) ---
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
// --- IMPORT UI (UNIQUEMENT MATERIAL 3) ---
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- IMPORT NAVIGATION ---
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ==========================================
// 1. CONFIGURATION NAVIGATION & COULEURS
// ==========================================

val ArkeaRed = Color(0xFFD31D29)
val BridgeTeal = Color(0xFF268694)
val BackgroundGray = Color(0xFFF5F5F5)

@Composable
fun ArkeaAuthApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }
    }
}

// ==========================================
// 2. UI KIT (COMPOSANTS)
// ==========================================

@Composable
fun ArkeaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = ArkeaRed),
        // Force le contenu pour éviter les bugs de scope
        content = {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    )
}

@Composable
fun ArkeaInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = icon?.let {
            { Icon(imageVector = it, contentDescription = null, tint = Color.Gray) }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ArkeaRed,
            focusedLabelColor = ArkeaRed,
            cursorColor = ArkeaRed,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}

// ==========================================
// 3. ÉCRAN DE CONNEXION
// ==========================================

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // En-tête Logo
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ARKEO BANQUE", color = ArkeaRed, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("ENTREPRISES & INSTITUTIONNELS", fontSize = 10.sp, fontWeight = FontWeight.Medium)
        }

        // Zone Image "Pont"
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BridgeTeal),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("DE NOUVEAUX\nLIENS POUR", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                // Label rouge
                Surface(color = ArkeaRed, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(" ÉCHANGER ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
                Text("INNOVER DEMAIN", color = Color.White, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }

        // Zone Actions
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            ArkeaButton(text = "Se connecter", onClick = { /* TODO Login */ })

            Spacer(modifier = Modifier.height(16.dp))

            // Lien vers Register
            TextButton(onClick = onNavigateToRegister) {
                Text("Ouvrir un compte", color = ArkeaRed, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

// ==========================================
// 4. ÉCRAN D'INSCRIPTION
// ==========================================

@Composable
fun RegisterScreen(onBackToLogin: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(BackgroundGray)) {
        // Header Rouge
        Box(
            modifier = Modifier.fillMaxWidth().background(ArkeaRed).padding(top = 48.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("OUVERTURE DE COMPTE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        // Carte Formulaire
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
                Text("VOS COORDONNÉES", color = ArkeaRed, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                ArkeaInput("", {}, "Raison Sociale", icon = Icons.Outlined.Person)
                ArkeaInput("", {}, "Email / Identifiant", keyboardType = KeyboardType.Email)
                ArkeaInput("", {}, "Mot de passe", icon = Icons.Outlined.Lock, isPassword = true)

                Spacer(modifier = Modifier.height(8.dp))
                ArkeaButton(text = "VALIDER LA DEMANDE", onClick = { })
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 24.dp)
        ) {
            Text("Annuler et retour", color = ArkeaRed)
        }
    }
}

@Composable
fun FooterItem(icon: ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = Color.DarkGray)
        Text(text, fontSize = 10.sp, textAlign = TextAlign.Center, lineHeight = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArkeaAuthApp()
}