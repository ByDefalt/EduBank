package defalt.network.api

import defalt.network.BuildConfig
import defalt.network.api.account.service.AccountApi
import defalt.network.api.account.service.PersonalInformationApi
import defalt.network.api.account.service.RoleApi
import defalt.network.api.bank.service.BankAccountApi
import defalt.network.api.bank.service.BankAccountParameterApi
import defalt.network.api.bank.service.BankAccountPivotApi
import defalt.network.api.bank.service.TypeApi
import defalt.network.api.offer.service.OfferApi
import defalt.network.api.operation.service.BeneficiaryApi
import defalt.network.api.operation.service.OperationApi
import defalt.network.infrastructure.ApiClient
import defalt.network.utils.createService
import org.koin.dsl.module

val networkModule = module {

    single {
        ApiClient(baseUrl = BuildConfig.GATEWAY_URL).setLogger { println(it) }
    }

    single { get<ApiClient>().createService<AccountApi>() }
    single { get<ApiClient>().createService<PersonalInformationApi>() }
    single { get<ApiClient>().createService<RoleApi>() }

    single { get<ApiClient>().createService<BankAccountApi>() }
    single { get<ApiClient>().createService<BankAccountParameterApi>() }
    single { get<ApiClient>().createService<BankAccountPivotApi>() }
    single { get<ApiClient>().createService<TypeApi>() }

    single { get<ApiClient>().createService<OfferApi>() }

    single { get<ApiClient>().createService<BeneficiaryApi>() }
    single { get<ApiClient>().createService<OperationApi>() }
}
