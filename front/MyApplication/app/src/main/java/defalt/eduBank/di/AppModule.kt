package defalt.eduBank.di

import defalt.core.di.coreModule
import defalt.featureAccount.di.accountModule
import defalt.featureBank.di.bankModule
import defalt.featureOffer.di.offerModule
import defalt.featureOperation.di.operationModule

val appModule = coreModule + accountModule + offerModule + bankModule + operationModule
