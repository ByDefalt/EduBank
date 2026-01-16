package defalt.core.di

import org.koin.dsl.module
import defalt.core.api.account.service.DefaultApi as AccountApi


val coreModule = listOf(
    // Network modules pour toutes les APIs de core
    networkModule<AccountApi>(BuildConfig.API_BASE_URL, "gatewayClient"),
    networkModule<BankApi>(BuildConfig.API_BASE_URL, "gatewayClient"),
    networkModule<OfferApi>(BuildConfig.API_BASE_URL, "gatewayClient"),
    networkModule<OperationApi>(BuildConfig.API_BASE_URL, "gatewayClient"),

    // Autres modules core (repositories, utils…)
    module {
        // Exemples de repositories globaux
        // single { AccountRepository(get()) }
        // single { BankRepository(get()) }
    }
)