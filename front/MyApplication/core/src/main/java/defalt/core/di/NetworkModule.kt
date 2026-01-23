package defalt.core.di

import defalt.core.BuildConfig.GATEWAY_URL
import defalt.core.infrastructure.ApiClient
import defalt.core.utils.createService
import org.koin.dsl.module
import defalt.core.api.account.service.AccountApi
import defalt.core.api.account.service.PersonalInformationApi
import defalt.core.api.bank.service.BankAccountApi
import defalt.core.api.bank.service.BankAccountParameterApi
import defalt.core.api.bank.service.TypeApi
import defalt.core.api.offer.service.OfferApi
import defalt.core.api.operation.service.BeneficiaryApi
import defalt.core.api.operation.service.OperationApi

val networkModule = module {

    single { ApiClient(baseUrl = GATEWAY_URL).setLogger { println(it) } }

    single { get<ApiClient>().createService<AccountApi>() }
    single { get<ApiClient>().createService<PersonalInformationApi>() }
    single { get<ApiClient>().createService<BankAccountApi>() }
    single { get<ApiClient>().createService<BankAccountParameterApi>() }
    single { get<ApiClient>().createService<TypeApi>() }
    single { get<ApiClient>().createService<OfferApi>() }
    single { get<ApiClient>().createService<BeneficiaryApi>() }
    single { get<ApiClient>().createService<OperationApi>() }
}
