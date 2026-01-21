package defalt.eduBank.di

import defalt.core.di.coreModule
import defalt.feature_account.di.accountModule

val appModule = coreModule + accountModule
