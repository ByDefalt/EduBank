package defalt.featureAccount.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.featureAccount.ui.screen.LoginScreen
import defalt.featureAccount.ui.screen.RegisterScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.accountGraph(
    onBackToHome: () -> Unit
) {
    navigation<Routes.Account>(
        startDestination = Routes.Account.Login,
    ) {
        composable<Routes.Account.Login> {
            LoginScreen(onBackToHome = onBackToHome)
        }
        composable<Routes.Account.Register> {
            RegisterScreen(onBackToHome = onBackToHome)
        }
    }
}
