package defalt.domain.di

import defalt.domain.repository.repositoryModule
import defalt.domain.session.sessionModule

/** Module racine de core:domain — Repositories */
val coreDomainModule = listOf(repositoryModule, sessionModule)
