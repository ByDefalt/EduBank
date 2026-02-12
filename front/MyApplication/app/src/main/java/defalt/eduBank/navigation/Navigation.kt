package defalt.eduBank.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import defalt.core.ui.screen.HomeScreen
import defalt.feature_account.ui.navigation.accountGraph

// Module: :app

// Module :app
@Composable
fun ArkeoNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Route directe pour le core (simple)
        composable("home") {
            HomeScreen(
                onNavigateToLogin = { navController.navigate("account_root") }, // On vise le graphe entier
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Appel de l'extension du module :feature_account
        // Cette fonction contient les routes "login" et "register"
        accountGraph(navController)
    }
}