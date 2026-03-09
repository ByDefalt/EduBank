package defalt.eduBank.di

import defalt.database.di.databaseModule
import defalt.domain.di.coreDomainModule
import defalt.featureAccount.di.accountModule
import defalt.featureBank.di.bankModule
import defalt.featureOffer.di.offerModule
import defalt.featureOperation.di.operationModule
import defalt.network.di.coreNetworkModule
import defalt.utils.coreUtilsModule

val appModule =
    coreNetworkModule +
        coreDomainModule +
        coreUtilsModule +
        databaseModule +
        accountModule +
        bankModule +
        offerModule +
        operationModule
