package config

import handler.AuthHandler
import handler.SignHandler
import handler.SignReactionHandler
import handler.SignReportHandler
import handler.UserHandler
import io.ktor.server.application.*
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import model.sign_report.SignReport
import org.koin.ktor.ext.inject
import repository.Implementation.UserRepositoryImplementation
import routing.authRoutes
import routing.signReactionRoutes
import routing.signReportRoutes
import routing.signRoutes
import routing.specificationRoutes
import routing.userRoutes
import service.AuthService
import service.JWTService
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.github.smiley4.ktorswaggerui.dsl.*

fun Application.configureRouting() {
    val authHandler by inject<AuthHandler>()
    val signHandler by inject<SignHandler>()
    val userHandler by inject<UserHandler>()
    val signReactionHandler by inject<SignReactionHandler>()
    val signReportHandler by inject<SignReportHandler>()
    routing {

        route("/health") {
            get({
                response {
                    HttpStatusCode.OK to {
                        description = "Health check successful"
                        body<String>()
                    }
                }
            }) {
                call.respondText("OK")
            }
        }

        route("/api") {
            specificationRoutes()
            authRoutes(authHandler)
            signRoutes(signHandler)
            userRoutes(userHandler)
            signReactionRoutes(signReactionHandler)
            signReportRoutes(signReportHandler)
        }
    }
}