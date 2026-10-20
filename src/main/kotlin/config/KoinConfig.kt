package config

import di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.installKoin(config: AppConfig) {
    install(Koin) {
        slf4jLogger()
        modules(appModule(config))
    }
}