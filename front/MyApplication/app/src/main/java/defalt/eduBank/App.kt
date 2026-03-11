package defalt.eduBank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import defalt.domain.entity.account.RoleEnum
import defalt.eduBank.ui.navigation.ArkeoNavHost
import defalt.featureAccount.viewModel.AuthViewModel
import defalt.ui.state.UiState
import defalt.ui.utils.CustomColor
import defalt.ui.utils.Routes
import org.koin.androidx.compose.koinViewModel

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArkeoApp(isPreview = false)
        }
    }
}
@Composable
fun ArkeoApp(isPreview: Boolean) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    if (!isPreview) {
        LaunchedEffect(Unit) {
            authViewModel.connect()
        }
    }

    val startDestination = when {
        authState is UiState.Success && (authState as UiState.Success<*>).data == RoleEnum.ADMIN -> Routes.Core.AdminHome
        authState is UiState.Success && (authState as UiState.Success<*>).data == RoleEnum.CUSTOMER -> Routes.Bank
        else -> null // pas encore résolu
    }

    Box(
        modifier = Modifier.fillMaxSize().background(CustomColor.ArkeoRed),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (startDestination != null || authState is UiState.Error || isPreview) {
                // On affiche le NavHost seulement quand on sait où aller
                ArkeoNavHost(
                    navController = navController,
                    startDestination = startDestination ?: Routes.Core.Home
                )
            } else {
                // Splash / loading pendant la vérification
                SplashScreen()
            }
        }
    }
}
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(CustomColor.ArkeoRed),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArkeoApp(isPreview = true)
}
