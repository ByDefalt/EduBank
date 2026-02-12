package defalt.eduBank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import defalt.core.ui.screen.HomeScreen
import defalt.eduBank.di.appModule
import defalt.feature_account.ui.screen.LoginScreen
import defalt.feature_account.ui.screen.RegisterScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            androidContext(this@App)
            modules(*appModule.toTypedArray())
        }
        enableEdgeToEdge()
        setContent {
            ArkeaApp()
        }
    }
}

@Composable
fun ArkeaApp() {
    val navController = rememberNavController()

    // Le point de départ est maintenant "home"
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("login") {
            LoginScreen(
                onBackToHome = { navController.popBackStack() }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackToHome = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArkeaApp()
}