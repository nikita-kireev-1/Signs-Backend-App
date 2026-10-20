package config

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureSpecification(

) {
    install(SwaggerUI) {
        info {
            title = "My API"
            version = "1.0.0"
            description = "API description"
        }
    }
}