package defalt.network.di

import defalt.network.api.networkModule
import defalt.network.datasource.dataSourcesModule

/** Module racine de core:network — APIs + DataSources */
val coreNetworkModule = listOf(networkModule, dataSourcesModule)
