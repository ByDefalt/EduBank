package defalt.featureOffer.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.core.ui.utils.Routes
import defalt.featureOffer.ui.screen.OffersScreen

fun NavGraphBuilder.offerGraph(navController: NavController) {
    navigation<Routes.Offer>(
        startDestination = Routes.Offer.List,
    ) {
        composable<Routes.Offer.List> {
            OffersScreen(onBack = { navController.popBackStack() })
        }
    }
}
