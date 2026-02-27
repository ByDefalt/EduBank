package defalt.featureOffer.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import defalt.featureOffer.ui.screen.OffersScreen
import defalt.ui.utils.Routes

fun NavGraphBuilder.offerGraph(
    onBack: () -> Unit,
) {
    navigation<Routes.Offer>(
        startDestination = Routes.Offer.List,
    ) {
        composable<Routes.Offer.List> {
            OffersScreen(onBack = onBack)
        }
    }
}
