package routing

import handler.UserHandler
import io.ktor.http.ContentType
import io.github.smiley4.ktorswaggerui.dsl.delete as documentedDelete
import io.github.smiley4.ktorswaggerui.dsl.get as documentedGet
import io.github.smiley4.ktorswaggerui.dsl.patch as documentedPatch
import io.github.smiley4.ktorswaggerui.dsl.post as documentedPost
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import model.ErrorResponse
import model.user.CreateUserRequest
import model.user.CreateUserResponse
import model.user.DeleteUserByIdRequest
import model.user.DeleteUserByUsernameRequest
import model.user.SearchUserByIdRequest
import model.user.SearchUserByUsernameRequest
import model.user.SearchUserResponse
import model.user.UpdateUserRequest
import model.user.UpdateUserResponse

fun Route.userRoutes(handler: UserHandler) {
    route("/user") {

        authenticate("auth-jwt") {

            documentedPost("", {
                description = "Create a user"

                request {
                    body<CreateUserRequest>(){
                        description = "username and password"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.Created to {
                        description = "User created successfully"
                        body<CreateUserResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid user data"
                    }
                    HttpStatusCode.Conflict to {
                        description = "User already exists"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.createUser(call)
            }

            documentedPatch("/id", {
                description = "Update a user by their identifier"

                request {
                    body<UpdateUserRequest>(){
                        description = "Username and password"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User data updated successfully"
                        body<UpdateUserResponse>()
                    }
                    HttpStatusCode.NotFound to {
                        description = "User not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.updateUser(call)
            }

            documentedDelete("/id", {
                description = "Delete a user by their identifier"

                request {
                    body<DeleteUserByIdRequest>(){
                        description = "id"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "User not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteUserById(call)
            }

            documentedDelete("/username", {
                description = "Delete a user by their username"

                request{
                    body<DeleteUserByUsernameRequest>(){
                        description = "username"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "User not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteUserByUsername(call)
            }
        }

        documentedGet("/id", {
            description = "Find a user by their identifier"

            request{
                body<SearchUserByIdRequest>(){
                    description = "Id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "User found"
                    body<SearchUserResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "User not found"
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid id"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchUserById(call)
        }

        documentedGet("/username", {
            description = "Find a user by their username"

            request{
                body<SearchUserByUsernameRequest>(){
                    description = "username"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "User found"
                    body<SearchUserResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "User not found"
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid username"
                    body<ErrorResponse>()
                }
                HttpStatusCode.Conflict to {
                    description = "Invalid data"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchUserByUsername(call)
        }

        documentedGet("/getAll", {
            description = "Get all users"

            response {
                HttpStatusCode.OK to {
                    description = "List of users retrieved successfully"
                    body<List<SearchUserResponse>>()
                }
            }
        }) {
            handler.getAll(call)
        }
    }
}