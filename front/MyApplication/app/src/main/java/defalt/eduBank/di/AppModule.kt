package defalt.eduBank.di

import defalt.domain.di.coreDomainModule
import defalt.featureAccount.di.accountModule
import defalt.featureBank.di.bankModule
import defalt.featureOffer.di.offerModule
import defalt.featureOperation.di.operationModule
import defalt.network.di.coreNetworkModule

val appModule =
    coreNetworkModule +
        coreDomainModule +
        accountModule +
        bankModule +
        offerModule +
        operationModule
