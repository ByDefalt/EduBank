package defalt.utils.logger

import org.koin.dsl.module

val loggerModule = module {
    single<Logger> { ConsoleLogger(LogLevel.DEBUG) }
}
