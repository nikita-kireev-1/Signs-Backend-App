package routing

import handler.SignReportHandler
import io.ktor.http.ContentType
import io.github.smiley4.ktorswaggerui.dsl.delete as documentedDelete
import io.github.smiley4.ktorswaggerui.dsl.get as documentedGet
import io.github.smiley4.ktorswaggerui.dsl.patch as documentedPatch
import io.github.smiley4.ktorswaggerui.dsl.post as documentedPost
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.contentType
import io.ktor.server.routing.route
import model.ErrorResponse
import model.sign_reaction.CreateSignReactionRequest
import model.sign_report.CreateSignReportRequest
import model.sign_report.CreateSignReportResponse
import model.sign_report.DeleteSignReportByIdRequest
import model.sign_report.DeleteSignReportBySignAndUserIdRequest
import model.sign_report.DeleteSignReportBySignIdRequest
import model.sign_report.DeleteSignReportByUserIdRequest
import model.sign_report.DeleteSignReportResponse
import model.sign_report.SearchSignReportByIdRequest
import model.sign_report.SearchSignReportBySignAndUserIdRequest
import model.sign_report.SearchSignReportBySignIdRequest
import model.sign_report.SearchSignReportByUserIdRequest
import model.sign_report.SearchSignReportResponse
import model.sign_report.UpdateSignReportRequest
import model.sign_report.UpdateSignReportResponse

fun Route.signReportRoutes(handler: SignReportHandler) {
    route("/signReport") {

        authenticate("auth-jwt") {

            documentedPost("", {
                description = "Create a report for a sign"

                request {
                    body<CreateSignReportRequest>() {
                        description = "signId and reason"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.Created to {
                        description = "Report created successfully"
                        body<CreateSignReportResponse>()
                    }
                    HttpStatusCode.BadRequest to {
                        description = "Invalid report data"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.createSignReport(call)
            }

            documentedPatch("/id", {
                description = "Update a report by its identifier"

                request {
                    body<UpdateSignReportRequest>() {
                        description = "id and reason"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Report updated successfully"
                        body<UpdateSignReportResponse>()
                    }
                    HttpStatusCode.NotFound to {
                        description = "Report not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.updateSignReport(call)
            }

            documentedDelete("/id", {
                description = "Delete a report by its identifier"

                request{
                    body<DeleteSignReportByIdRequest>() {
                        description = "id"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Report deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "Report not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteSignReportById(call)
            }

            documentedDelete("/userId", {
                description = "Delete a user's reports"

                request{
                    body<DeleteSignReportByUserIdRequest>(){
                        description = "userId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "User's reports deleted successfully"
                        body<DeleteSignReportResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteSignReportByUserId(call)
            }

            documentedDelete("/signId", {
                description = "Delete reports for a sign"

                request{
                    body<DeleteSignReportBySignIdRequest>() {
                        description = "signId"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Reports for the sign deleted successfully"
                        body<DeleteSignReportResponse>()
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteSignReportBySignId(call)
            }

            documentedDelete("/userAndSignId", {
                description = "Delete a user's report for a specific sign"

                request{
                    body<DeleteSignReportBySignAndUserIdRequest>() {
                        description = "user and sign Id"
                        required = true
                        mediaType(ContentType.Application.Json)
                    }
                }

                response {
                    HttpStatusCode.OK to {
                        description = "Report deleted successfully"
                    }
                    HttpStatusCode.NotFound to {
                        description = "Report not found"
                    }
                    HttpStatusCode.Unauthorized to {
                        description = "User is not authenticated"
                    }
                }
            }) {
                handler.deleteSignReportByUserAndSignId(call)
            }
        }

        documentedGet("/id", {
            description = "Find a report by its identifier"

            request{
                body<SearchSignReportByIdRequest>() {
                    description = "Id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Report found"
                    body<SearchSignReportResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "Report not found"
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
            handler.searchSignReportById(call)
        }

        documentedGet("/userId", {
            description = "Get a user's reports"

            request{
                body<SearchSignReportByUserIdRequest>(){
                    description = "userId"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "User's reports retrieved successfully"
                    body<List<SearchSignReportResponse>>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid user id"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReportByUserId(call)
        }

        documentedGet("/signId", {
            description = "Get reports for a sign"

            request{
                body<SearchSignReportBySignIdRequest>(){
                    description = "SignId"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Reports for the sign retrieved successfully"
                    body<List<SearchSignReportResponse>>()
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "invalid sign Id"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReportBySignId(call)
        }

        documentedGet("/userAndSignId", {
            description = "Find a user's report for a specific sign"

            request{
                body<SearchSignReportBySignAndUserIdRequest>(){
                    description = "Sign id and user Id"
                    required = true
                    mediaType(ContentType.Application.Json)
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Report found"
                    body<SearchSignReportResponse>()
                }
                HttpStatusCode.NotFound to {
                    description = "Report not found"
                }
                HttpStatusCode.UnsupportedMediaType to {
                    description = "Request body is missing or has an unsupported media type"
                }
                HttpStatusCode.BadRequest to {
                    description = "Invalid user and sign id"
                    body<ErrorResponse>()
                }
            }
        }) {
            handler.searchSignReportByUserAndSignId(call)
        }

        documentedGet("/getAll", {
            description = "Get all reports"

            response {
                HttpStatusCode.OK to {
                    description = "List of reports retrieved successfully"
                    body<List<SearchSignReportResponse>>()
                }
            }
        }) {
            handler.getAll(call)
        }
    }
}