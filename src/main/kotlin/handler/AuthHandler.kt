package handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import model.auth.LoginRequest
import model.auth.LoginResponse
import model.auth.RegisterRequest
import model.auth.RegisterResponse
import org.slf4j.LoggerFactory
import service.AuthService
import service.interfaces.AuthServiceInterface

class AuthHandler(
    private val authService: AuthServiceInterface
) {
    private val logger = LoggerFactory.getLogger(AuthHandler::class.java)
    suspend fun login(
        call: ApplicationCall
    ) {
        logger.info("Starting login")
        val request = call.receive<LoginRequest>()

        val token =
            authService.login(
                username = request.username,
                password = request.password
            )

        call.respond(
            HttpStatusCode.OK,
            LoginResponse(
                accessToken = token
            )
        )
        logger.info("Ending login")
    }
    suspend fun register(
        call : ApplicationCall
    ){
        logger.info("Starting register")
        val request = call.receive<RegisterRequest>()

        val token =
            authService.register(
                username = request.username,
                password = request.password
            )

        call.respond(
            HttpStatusCode.Created,
            RegisterResponse(
                accessToken = token
            )
        )
        logger.info("Ending register")
    }
}