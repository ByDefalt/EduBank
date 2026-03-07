package defalt.domain.session

import org.koin.dsl.module

var sessionModule = module {
    single { Session() } 
}