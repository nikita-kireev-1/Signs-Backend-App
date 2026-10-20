package routing

import handler.SignHandler
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
import model.sign.CreateSignRequest
import model.sign.CreateSignResponse
import model.sign.DeleteSignByIdRequest
import model.sign.DeleteSignsByAuthorIdRequest
import model.sign.DeleteSignsByAuthorIdResponse
import model.sign.SearchSignByIdRequest
import model.sign.SearchSignByLocationRequest
import model.sign.SearchSignResponse
import model.sign.SearchSignsByAuthorIdRequest
import model.sign.UpdateSignRequest
import model.sign.UpdateSignResponse

fun Route.signRoutes(handler: SignHandler) {
    route("/sign") {

        authenticate("auth-jwt") {

            documentedPost("", {
                description = "Create a sign on the map"

                request {
                    body<CreateSignRequest>(){
                        description = "latitude, longitude, text, expiresAt"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.Created to {
                        description = "Sign created successfully"
                        body<CreateSignResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid sign data"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.createSign(call)
            }

            documentedPatch("/id", {
                description = "Update a sign by its identifier"

                request {
                    body<UpdateSignRequest>(){
                        description = "id, text, status, longitude, latitude, expiresAt"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Sign updated successfully"
                        body< UpdateSignResponse>()
                    }
                    HttpStatusCode.NotFound to {
                        description = "Sign not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.updateSign(call)
            }

            documentedDelete("/id", {
                description = "Delete a sign by its identifier"

                request {
                    body<DeleteSignByIdRequest>(){
                        description = "id"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Sign deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "Sign not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteById(call)
            }

            documentedDelete("/authorId", {
                description = "Delete signs created by an author"

                request{
                    body<DeleteSignsByAuthorIdRequest>(){
                        description = "authorId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Author's signs deleted successfully"
                        body<DeleteSignsByAuthorIdResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteByAuthorId(call)
            }
        }

        documentedGet("/id", {
            description = "Find a sign by its identifier"

            request{
                body<SearchSignByIdRequest>(){
                    description = "id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Sign found"
                    body<SearchSignResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "Sign not found"
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid id format"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchById(call)
        }

        documentedGet("/authorId", {
            description = "Get signs created by an author"

            request{
                body<SearchSignsByAuthorIdRequest>(){
                    description = "AuthorId"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Author's signs retrieved successfully"
                    body<List<SearchSignResponse>>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid authorId format"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchByAuthorId(call)
        }

        documentedGet("/location", {
            description = "Find signs within a specified map area"

            request{
                body<SearchSignByLocationRequest>(){
                    description = "latitude,longitude"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Signs within the specified area retrieved successfully"
                    body<List<SearchSignResponse>>()
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid coordinates"
                    body<ErrorResponse>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
            }
        }) {
            handler.searchByLocation(call)
        }

        documentedGet("/getAll", {
            description = "Get all signs"

            response {
                HttpStatusCode.OK to {
                    description = "List of signs retrieved successfully"
                    body<List<SearchSignResponse>>()
                }
            }
        }) {
            handler.getAll(call)
        }
    }
}