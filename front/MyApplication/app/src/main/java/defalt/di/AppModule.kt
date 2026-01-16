package defalt.di

import defalt.core.di.coreModule
import defalt.core.di.networkModule
import org.koin.dsl.module


val AppModule = listOf(
    // Modules Core
    *coreModule.toTypedArray(),

    // Si tu as d'autres modules spécifiques à l'application ou features
    // example: featureModule
    // featureModule1,
    // featureModule2
)
