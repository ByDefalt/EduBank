package defalt.network.di

import defalt.network.BuildConfig
import defalt.network.api.networkModule
import defalt.network.datasource.dataSourcesModule
import defalt.network.fake.fakeDataSourcesModule

/** Module racine de core:network — APIs + DataSources */

val sourceModule = if (!BuildConfig.FAKE_DATA_IS_ACTIVE) dataSourcesModule else fakeDataSourcesModule

val coreNetworkModule = listOf(networkModule, sourceModule)
