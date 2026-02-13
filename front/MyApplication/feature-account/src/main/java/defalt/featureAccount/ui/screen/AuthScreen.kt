package defalt.featureAccount.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import defalt.core.ui.screen.HomeScreen

@Composable
fun ArkeaApp() {
    val navController = rememberNavController()

    // Le point de départ est maintenant "home"
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") },
            )
        }
        composable("login") {
            LoginScreen(
                onBackToHome = { navController.popBackStack() },
            )
        }
        composable("register") {
            RegisterScreen(
                onBackToHome = { navController.popBackStack() },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArkeaApp()
}
