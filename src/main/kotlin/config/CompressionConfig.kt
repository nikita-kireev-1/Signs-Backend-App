package config

import io.ktor.server.plugins.compression.*
import io.ktor.http.content.*
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureCompression() {
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }
}