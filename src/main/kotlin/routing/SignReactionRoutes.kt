package routing

import handler.SignReactionHandler
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.github.smiley4.ktorswaggerui.dsl.post as documentedPost
import io.ktor.http.HttpStatusCode
import model.ErrorResponse
import model.sign_reaction.CreateSignReactionRequest
import model.sign_reaction.CreateSignReactionResponse
import model.sign_reaction.DeleteSignReactionByIdRequest
import model.sign_reaction.DeleteSignReactionBySignAndUserIdRequest
import model.sign_reaction.DeleteSignReactionBySignIdRequest
import model.sign_reaction.DeleteSignReactionByUserIdRequest
import model.sign_reaction.DeleteSignReactionResponse
import model.sign_reaction.SearchSignReactionByIdRequest
import model.sign_reaction.SearchSignReactionBySignIdRequest
import model.sign_reaction.SearchSignReactionByUserAndSignIdRequest
import model.sign_reaction.SearchSignReactionByUserIdRequest
import model.sign_reaction.SearchSignReactionResponse
import model.sign_reaction.UpdateSignReactionRequest
import model.sign_reaction.UpdateSignReactionResponse
import javax.net.ssl.HttpsURLConnection
import io.github.smiley4.ktorswaggerui.dsl.patch as documentedPatch
import io.github.smiley4.ktorswaggerui.dsl.delete as documentedDelete
import io.github.smiley4.ktorswaggerui.dsl.get as documentedGet

fun Route.signReactionRoutes(handler: SignReactionHandler) {
    route("/signReaction") {

        authenticate("auth-jwt") {

            documentedPost("", {
                description = "Create a reaction to a sign"

                request {
                    body<CreateSignReactionRequest>() {
                        description = "signId and type of reaction"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.Created to {
                        description = "Reaction created successfully"
                        body<CreateSignReactionResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid reaction data"
                        body<ErrorResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.createSignReaction(call)
            }

            documentedPatch("/id", {
                description = "Update a reaction by its identifier"

                request {
                    body<UpdateSignReactionRequest>() {
                        description = "id and type of reaction"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Reaction updated successfully"
                        body<UpdateSignReactionResponse>()
                    }
                    HttpStatusCode.NotFound to {
                        description = "Reaction not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }

                }
            }) {
                handler.updateSignReaction(call)
            }

            documentedDelete("/id", {
                description = "Delete a reaction by its identifier"

                request{
                    body<DeleteSignReactionByIdRequest>() {
                        description = "id"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Reaction deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "Reaction not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteSignReactionById(call)
            }

            documentedDelete("/userId", {
                description = "Delete a user's reactions"

                request{
                    body<DeleteSignReactionByUserIdRequest>() {
                        description = "userId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User's reactions deleted successfully"
                        body<DeleteSignReactionResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid user Id"
                        body<ErrorResponse>()
                    }
                }
            }) {
                handler.deleteSignReactionByUserId(call)
            }

            documentedDelete("/signId", {
                description = "Delete reactions to a sign"

                request{
                    body<DeleteSignReactionBySignIdRequest>() {
                        description = "signId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Reactions to the sign deleted successfully"
                        body<DeleteSignReactionResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid sign id"
                        body<ErrorResponse>()
                    }
                }
            }) {
                handler.deleteSignReactionBySignId(call)
            }

            documentedDelete("/userAndSignId", {
                description = "Delete a user's reaction to a specific sign"

                request{
                    body<DeleteSignReactionBySignAndUserIdRequest>() {
                        description = "SignId and UserId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User's reaction to the sign deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "Reaction not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid user and sign id"
                        body<ErrorResponse>()
                    }
                }
            }) {
                handler.deleteSignReactionBySignAndUserId(call)
            }
        }

        documentedGet("/id", {
            description = "Find a reaction by its identifier"

            request{
                body<SearchSignReactionByIdRequest>(){
                    description = "id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Reaction found"
                    body<SearchSignReactionResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "Reaction not found"
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
            handler.searchSignReactionById(call)
        }

        documentedGet("/userId", {
            description = "Get a user's reactions"

            request{
                body<SearchSignReactionByUserIdRequest>() {
                    description = "userId"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "User's reactions retrieved successfully"
                    body<List<SearchSignReactionResponse>>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid data"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReactionByUserId(call)
        }

        documentedGet("/signId", {
            description = "Get reactions to a sign"

            request{
                body<SearchSignReactionBySignIdRequest>() {
                    description = "signId"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Reactions to the sign retrieved successfully"
                    body<List<SearchSignReactionResponse>>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid data"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReactionBySignId(call)
        }

        documentedGet("/userAndSignId", {
            description = "Find a user's reaction to a specific sign"

            request{
                body<SearchSignReactionByUserAndSignIdRequest>() {
                    description = "sign and user id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Reaction found"
                    body<SearchSignReactionResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "Reaction not found"
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid request body"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReactionBySignAndUserId(call)
        }

        documentedGet("/getAll", {
            description = "Get all reactions"

            response {
                HttpStatusCode.OK to {
                    description = "List of reactions retrieved successfully"
                    body<List<SearchSignReactionResponse>>()
                }
            }
        }) {
            handler.getAll(call)
        }
    }
}