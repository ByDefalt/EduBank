package defalt.eduBank.di

import defalt.featureAccount.di.accountModule
import defalt.featureBank.di.bankModule
import defalt.featureOffer.di.offerModule
import defalt.featureOperation.di.operationModule
import defalt.network.di.networkModule

val appModule = networkModule + accountModule + offerModule + bankModule + operationModule
