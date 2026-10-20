package handler

import exception.UnauthorizedException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import model.sign.CreateSignRequest
import model.sign.DeleteSignByIdRequest
import model.sign.DeleteSignsByAuthorIdRequest
import model.sign.DeleteSignsByAuthorIdResponse
import model.sign.SearchSignByIdRequest
import model.sign.SearchSignByLocationRequest
import model.sign.SearchSignsByAuthorIdRequest
import model.sign.SignStatus
import model.sign.UpdateSignRequest
import org.slf4j.LoggerFactory
import service.SignService
import service.interfaces.SignServiceInterface
import java.time.OffsetDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SignHandler(
    private val signService: SignServiceInterface
) {
    private val logger = LoggerFactory.getLogger(SignHandler::class.java)
    suspend fun createSign(call : ApplicationCall){
        logger.info("Starting to create a sign")
        val request = call.receive<CreateSignRequest>()
        val userId = call.requireUserId()
        val sign = signService.create(
            userId = userId,
            authorId = userId,
            text = request.text,
            status = SignStatus.ACTIVE,
            longitude = request.longitude,
            latitude = request.latitude,
            expiresAt = OffsetDateTime.parse(request.expiresAt)
        )
        call.respond(HttpStatusCode.Created, sign.toCreateSignResponse())
        logger.info("Ending to create a sign")
    }
    suspend fun updateSign(call : ApplicationCall){
        logger.info("Starting to update a sign")
        val request = call.receive<UpdateSignRequest>()
        val userId = call.requireUserId()
        val sign = signService.update(
            userId = userId,
            id = Uuid.parse(request.id),
            text = request.text,
            status = request.status,
            longitude = request.longitude,
            latitude = request.latitude,
            expiresAt = OffsetDateTime.parse(request.expiresAt)
        )
        call.respond(HttpStatusCode.OK,sign.toUpdateSignResponse())
        logger.info("Ending to update a sign")
    }
    suspend fun deleteById(call : ApplicationCall){
        logger.info("Starting to delete a sign by id")
        val request = call.receive<DeleteSignByIdRequest>()
        val userId = call.requireUserId()
        val bool = signService.deleteById(Uuid.parse(request.id),userId)
        if(bool){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete a sign by id")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failing to find sign to delete by id")
        }
    }
    suspend fun deleteByAuthorId(call : ApplicationCall){
        logger.info("Starting to delete a sign by author id")
        val request = call.receive<DeleteSignsByAuthorIdRequest>()
        val userId = call.requireUserId()
        val counter = signService.deleteByAuthorId(userId,Uuid.parse(request.authorId))
        call.respond(
            HttpStatusCode.OK,
            DeleteSignsByAuthorIdResponse(
                counter
            )
        )
        logger.info("Ending to delete a sign by author id")
    }
    suspend fun searchById(call : ApplicationCall){
        logger.info("Starting to search for a sign by id")
        val request = call.receive<SearchSignByIdRequest>()
        val sign = signService.searchById(
            Uuid.parse(request.id)
        )
        call.respond(HttpStatusCode.OK,sign.toSearchSignResponse())
        logger.info("Ending to search for a sign by id")
    }
    suspend fun searchByAuthorId(call : ApplicationCall){
        logger.info("Starting to search for a sign by author id")
        val request = call.receive<SearchSignsByAuthorIdRequest>()
        val signs = signService.searchByAuthorId(
            Uuid.parse(request.authorId)
        )
        call.respond(HttpStatusCode.OK,signs.map{it.toSearchSignResponse()})
        logger.info("Ending to search for a sign by author id")
    }
    suspend fun searchByLocation(call : ApplicationCall){
        logger.info("Starting to search for a sign by location")
        val request = call.receive<SearchSignByLocationRequest>()
        val signs = signService.searchByLocation(
            request.longitude,
            request.latitude
        )
        call.respond(HttpStatusCode.OK,signs.map{it.toSearchSignResponse()})
        logger.info("Ending to search for a sign by location")
    }
    suspend fun getAll(call : ApplicationCall){
        logger.info("Starting selecting all signs in database")
        val signs = signService.getAll()
        call.respond(HttpStatusCode.OK,signs.map{it.toSearchSignResponse()})
        logger.info("Ending selecting all signs in database")
    }
}
fun ApplicationCall.requireUserId(): Uuid {
    val logger = LoggerFactory.getLogger(ApplicationCall::class.java)
    logger.info("Starting to get user id from JWT")
    val userId = principal<JWTPrincipal>()
        ?.payload
        ?.getClaim("userId")
        ?.asString()
    if(userId == null){
        logger.warn("Failing to get user id from JWT")
        throw UnauthorizedException()
    }else{
        logger.info("Ending getting user id from JWT")
        return Uuid.parse(userId)
    }
}