package handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import model.sign_report.CreateSignReportRequest
import model.sign_report.DeleteSignReportByIdRequest
import model.sign_report.DeleteSignReportBySignAndUserIdRequest
import model.sign_report.DeleteSignReportBySignIdRequest
import model.sign_report.DeleteSignReportByUserIdRequest
import model.sign_report.DeleteSignReportResponse
import model.sign_report.SearchSignReportByIdRequest
import model.sign_report.SearchSignReportBySignAndUserIdRequest
import model.sign_report.SearchSignReportBySignIdRequest
import model.sign_report.SearchSignReportByUserIdRequest
import model.sign_report.UpdateSignReportRequest
import org.jetbrains.exposed.v1.core.idParam
import org.slf4j.LoggerFactory
import service.interfaces.SignReportServiceInterface
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SignReportHandler(
    private val signReportService: SignReportServiceInterface
) {
    private val logger = LoggerFactory.getLogger(SignReportHandler::class.java)
    suspend fun createSignReport(
        call: ApplicationCall
    ){
        logger.info("Starting to create a sign report")
        val request = call.receive<CreateSignReportRequest>()
        val userId = call.requireUserId()
        val signReport = signReportService.create(
            userId,
            userId,
            Uuid.parse(request.signId),
            request.reason
        )
        call.respond(HttpStatusCode.Created,signReport.toCreateSignReportResponse())
        logger.info("Ending to create a sign report")
    }
    suspend fun updateSignReport(
        call: ApplicationCall
    ){
        logger.info("Starting to update a sign report")
        val request = call.receive<UpdateSignReportRequest>()
        val userId = call.requireUserId()
        val signReport = signReportService.update(
            requesterId = userId,
            id = Uuid.parse(request.id),
            reason = request.reason
        )
        call.respond(HttpStatusCode.OK,signReport.toUpdateSignReportResponse())
        logger.info("Ending to update a sign report")
    }
    suspend fun deleteSignReportById(
        call: ApplicationCall
    ){
        logger.info("Starting to delete sign report by id")
        val request = call.receive<DeleteSignReportByIdRequest>()
        val userId = call.requireUserId()
        val deleteConfirmed = signReportService.deleteById(
            userId,
            Uuid.parse(request.id)
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete a sign report by id")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete a sign report by id")
        }
    }
    suspend fun deleteSignReportBySignId(
        call: ApplicationCall
    ){
        logger.info("Starting to delete sign report by sign id")
        val request = call.receive<DeleteSignReportBySignIdRequest>()
        val userId = call.requireUserId()
        val deleteCounter = signReportService.deleteBySignId(
            userId,
            Uuid.parse(request.signId)
        )
        call.respond(HttpStatusCode.OK, DeleteSignReportResponse(
                deleteCounter
            )
        )
        logger.info("Ending to delete sign report by sign id")
    }
    suspend fun deleteSignReportByUserId(
        call: ApplicationCall
    ){
        logger.info("Starting to delete sign report by user id")
        val request = call.receive<DeleteSignReportByUserIdRequest>()
        val userId = call.requireUserId()
        val deleteCounter = signReportService.deleteByUserId(
            userId,
            Uuid.parse(request.userId)
        )
        call.respond(HttpStatusCode.OK, DeleteSignReportResponse(
                deleteCounter
            )
        )
        logger.info("Ending to delete sign report by user id")
    }
    suspend fun deleteSignReportByUserAndSignId(
        call: ApplicationCall
    ){
        logger.info("Starting to delete sign report by user and sign id")
        val request = call.receive<DeleteSignReportBySignAndUserIdRequest>()
        val userId = call.requireUserId()
        val deleteConfirmed = signReportService.deleteByUserAndSignId(
            userId,
            Uuid.parse(request.userId),
            Uuid.parse(request.signId)
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete sign report by user and sign id")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete sign report by user and sign id")
        }
    }
    suspend fun searchSignReportById(
        call: ApplicationCall
    ){
        logger.info("Starting to search sign report by id")
        val request = call.receive<SearchSignReportByIdRequest>()
        val signReport = signReportService.findById(Uuid.parse(request.id))
        call.respond(HttpStatusCode.OK,signReport.toSearchSignReportResponse())
        logger.info("Ending to search sign report by id")
    }
    suspend fun searchSignReportByUserId(
        call: ApplicationCall
    ){
        logger.info("Starting to search sign report by user id")
        val request = call.receive<SearchSignReportByUserIdRequest>()
        val signReports = signReportService.findByUserId(Uuid.parse(request.userId))
        call.respond(HttpStatusCode.OK,signReports.map{it.toSearchSignReportResponse()})
        logger.info("Ending to search sign report by user id")
    }
    suspend fun searchSignReportBySignId(
        call: ApplicationCall
    ){
        logger.info("Starting to search sign report by sign id")
        val request = call.receive<SearchSignReportBySignIdRequest>()
        val signReports = signReportService.findBySignId(Uuid.parse(request.signId))
        call.respond(HttpStatusCode.OK,signReports.map{it.toSearchSignReportResponse()})
        logger.info("Ending to search sign report by sign id")
    }
    suspend fun searchSignReportByUserAndSignId(
        call: ApplicationCall
    ){
        logger.info("Starting to search sign report by user and sign id")
        val request = call.receive<SearchSignReportBySignAndUserIdRequest>()
        val signReports = signReportService.findBySignAndUserId(Uuid.parse(request.userId),Uuid.parse(request.signId))
        call.respond(HttpStatusCode.OK, signReports.toSearchSignReportResponse())
        logger.info("Ending to search sign report by user and sign id")
    }
    suspend fun getAll(
        call: ApplicationCall
    ) {
        logger.info("Starting to get all sign reports")
        val signReports = signReportService.getAll()
        call.respond(HttpStatusCode.OK,signReports.map { it.toSearchSignReportResponse() })
        logger.info("Ending to get all sign reports")
    }
}