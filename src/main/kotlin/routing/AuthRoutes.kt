
package routing

import handler.AuthHandler
import io.github.smiley4.ktorswaggerui.dsl.post as documentedPost
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import model.ErrorResponse
import model.auth.LoginRequest
import model.auth.LoginResponse
import model.auth.RegisterRequest
import model.auth.RegisterResponse

fun Route.authRoutes(handler: AuthHandler) {
    route("/auth") {
        documentedPost("/login", {
            description = "User Authentication"

            request {
                body<LoginRequest> {
                    description = "Username and password"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Successful authentication"
                    body<LoginResponse>()
                }
                HttpStatusCode.BadRequest to {
                    description = "Username or password does not satisfy format restrictions"
                    body<ErrorResponse>()
                }
                HttpStatusCode.Conflict to {
                    description = "Invalid data"
                    body<ErrorResponse>()
                }
                HttpStatusCode.Unauthorized to {
                    description = "Invalid username or password"
                    body<ErrorResponse>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.NotFound to {
                    description = "Data not found"
                    body<ErrorResponse>()
                }

            }
        }) {
            handler.login(call)
        }

        documentedPost("/register", {
            description = "User registration"

            request {
                body<RegisterRequest> {
                    description = "username and password for registration"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.Created to {
                    description = "user successfully registered"
                    body<RegisterResponse>()
                }
                HttpStatusCode.BadRequest to {
                    description = "invalid data format"
                    body<ErrorResponse>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.Conflict to {
                    description = "Username already exists"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.register(call)
        }
    }
}
//TODO(ПОчти везде BadRequest добавить при нарушении согласования Field и schema для data class'ов)