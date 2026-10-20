package config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import service.JWTService
import service.interfaces.JWTServiceInterface

fun Application.configureSecurity(
    jwtService: JWTServiceInterface
) {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwtService.verifier())
            validate { credential ->
                val userId = credential
                    .payload
                    .getClaim("userId")
                    .asString()
                if (userId != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf(
                        "error" to "Invalid or expired token"
                    )
                )
            }
        }
    }
}