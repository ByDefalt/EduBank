package defalt.eduBank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import defalt.eduBank.di.appModule
import defalt.eduBank.ui.navigation.ArkeoNavHost
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
            ArkeoApp()
        }
    }
}

@Composable
fun ArkeoApp() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        ArkeoNavHost(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArkeoApp()
}
