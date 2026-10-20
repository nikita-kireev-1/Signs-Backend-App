package config

import exception.ForbiddenException
import exception.IdAlreadyExistsException
import exception.InvalidCredentialsException
import exception.ResourceNotFoundException
import exception.UnauthorizedException
import exception.UsernameAlreadyExistsException
import exception.ValidationException
import exception.WrongCredentialsException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import model.ErrorResponse

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = cause.message ?: "Invalid request")
            )
        }
        exception<NoSuchElementException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(message = cause.message ?: "Resource not found")
            )
        }
        exception<ForbiddenException>{ call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(message = cause.message ?: "Request forbidden")
            )
        }
        exception<IdAlreadyExistsException> { call, cause ->
            call.respond(
                HttpStatusCode.ExpectationFailed,
                ErrorResponse(message = cause.message ?: "Id already exists")
            )
        }
        exception<InvalidCredentialsException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    message = cause.message ?: "Invalid username or password format"
                )
            )
        }
        exception<WrongCredentialsException> { call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(
                    message = cause.message ?: "Invalid username or password"
                )
            )
        }
        exception<ResourceNotFoundException>{ call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(message = cause.message ?: "Resource wasn't found")
            )
        }
        exception<UnauthorizedException>{ call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse(message = cause.message ?: "Unauthorized user request")
            )
        }
        exception<UsernameAlreadyExistsException>{ call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(message = cause.message ?: "Username already exists")
            )
        }
        exception<ValidationException>{call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(message = cause.message ?: "Invalid data")
            )
        }
        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(message = cause.message ?: "Invalid request body")
            )
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(message = "Internal server error")
            )
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(message = "Endpoint not found")
            )
        }
    }
}