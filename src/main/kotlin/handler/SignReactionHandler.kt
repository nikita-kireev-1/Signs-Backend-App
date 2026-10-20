package handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import model.sign_reaction.CreateSignReactionRequest
import model.sign_reaction.DeleteSignReactionByIdRequest
import model.sign_reaction.DeleteSignReactionBySignAndUserIdRequest
import model.sign_reaction.DeleteSignReactionBySignIdRequest
import model.sign_reaction.DeleteSignReactionByUserIdRequest
import model.sign_reaction.DeleteSignReactionResponse
import model.sign_reaction.SearchSignReactionByIdRequest
import model.sign_reaction.SearchSignReactionBySignIdRequest
import model.sign_reaction.SearchSignReactionByUserAndSignIdRequest
import model.sign_reaction.SearchSignReactionByUserIdRequest
import model.sign_reaction.UpdateSignReactionRequest
import org.slf4j.LoggerFactory
import service.SignReactionService
import service.interfaces.SignReactionServiceInterface
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class SignReactionHandler(
    private val signReactionService: SignReactionServiceInterface
) {
    private val logger = LoggerFactory.getLogger(SignReactionHandler::class.java)
    suspend fun createSignReaction(
        call : ApplicationCall
    ) {
        logger.info("Starting to create a sign reaction")
        val request = call.receive<CreateSignReactionRequest>()
        val userId = call.requireUserId()
        val signReaction = signReactionService.create(
            userId = userId,
            signId = Uuid.parse(request.signId),
            type = request.type
        )
        call.respond(HttpStatusCode.Created,signReaction.toCreateSignReactionResponse())
        logger.info("Ending to create a sign reaction")
    }
    suspend fun updateSignReaction(
        call : ApplicationCall
    ){
        logger.info("Starting to update a sign reaction")
        val request = call.receive<UpdateSignReactionRequest>()
        val userId = call.requireUserId()
        val signReaction = signReactionService.update(
            Uuid.parse(request.id),
            userId,
            request.type,
        )
        call.respond(HttpStatusCode.OK,signReaction.toUpdateSignReactionResponse())
        logger.info("Ending to update a sign reaction")
    }
    suspend fun deleteSignReactionById(
        call : ApplicationCall
    ){
        logger.info("Starting to delete sign reaction by id")
        val request = call.receive<DeleteSignReactionByIdRequest>()
        val userId = call.requireUserId()
        val deleteConfirmed = signReactionService.deleteById(
            Uuid.parse(request.id),
            userId
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete a sign reaction by id")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete a sign reaction by id")
        }
    }
    suspend fun deleteSignReactionByUserId(
        call : ApplicationCall
    ){
        logger.info("Starting to delete sign reaction by user id")
        val request = call.receive<DeleteSignReactionByUserIdRequest>()
        val userId = call.requireUserId()
        val deleteCounter = signReactionService.deleteByUserId(
            userId,
            Uuid.parse(request.userId)
        )
        call.respond(HttpStatusCode.OK, DeleteSignReactionResponse(
                deleteCounter
            )
        )
        logger.warn("Ending to delete sign reaction by user id")
    }
    suspend fun deleteSignReactionBySignId(
        call : ApplicationCall
    ){
        logger.info("Starting to delete sign reaction by sign id")
        val request = call.receive<DeleteSignReactionBySignIdRequest>()
        val userId = call.requireUserId()
        val deleteCounter = signReactionService.deleteBySignId(
            userId,
            Uuid.parse(request.signId)
        )
        call.respond(HttpStatusCode.OK, DeleteSignReactionResponse(
                deleteCounter
            )
        )
        logger.warn("Ending to delete sign reaction by sign id")
    }
    suspend fun deleteSignReactionBySignAndUserId(
        call : ApplicationCall
    ){
        logger.info("Starting to delete sign reaction by sign and user id")
        val request = call.receive<DeleteSignReactionBySignAndUserIdRequest>()
        val userId = call.requireUserId()
        val deleteConfirmed = signReactionService.deleteByUserAndSignId(
            userId,
            Uuid.parse(request.userId),
            Uuid.parse(request.signId)
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete sign reaction by sign and user id")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete sign reaction by sign and user id")
        }
    }
    suspend fun searchSignReactionById(
        call : ApplicationCall
    ){
        logger.info("Starting to search for sign reaction by id")
        val request = call.receive<SearchSignReactionByIdRequest>()
        val signReaction = signReactionService.findById(Uuid.parse(request.id))
        call.respond(HttpStatusCode.OK,signReaction.toSearchSignReactionResponse())
        logger.info("Ending searching for sign reaction by id")
    }
    suspend fun searchSignReactionByUserId(
        call: ApplicationCall
    ){
        logger.info("Starting to search for sign reaction by user id")
        val request = call.receive<SearchSignReactionByUserIdRequest>()
        val signReaction = signReactionService.findByUserId(Uuid.parse(request.userId))
        call.respond(HttpStatusCode.OK,signReaction.map{it.toSearchSignReactionResponse()})
        logger.info("Ending searching for sign reaction by user id")
    }
    suspend fun searchSignReactionBySignId(
        call: ApplicationCall
    ){
        logger.info("Starting to search for sign reaction by sign id")
        val request = call.receive<SearchSignReactionBySignIdRequest>()
        val signReaction = signReactionService.findBySignId(Uuid.parse(request.signId))
        call.respond(HttpStatusCode.OK,signReaction.map{it.toSearchSignReactionResponse()})
        logger.info("Ending searching for sign reaction by sign id")
    }
    suspend fun searchSignReactionBySignAndUserId(
        call: ApplicationCall
    ){
        logger.info("Starting to search for sign reaction by sign and user id")
        val request = call.receive<SearchSignReactionByUserAndSignIdRequest>()
        val signReaction = signReactionService.findBySignAndUserId(Uuid.parse(request.userId),Uuid.parse(request.signId))
        call.respond(HttpStatusCode.OK,signReaction.toSearchSignReactionResponse())
        logger.info("Ending searching for sign reaction by sign and user id")
    }
    suspend fun getAll(
        call: ApplicationCall
    ){
        logger.info("Starting to getting all sign reactions")
        val signReactions = signReactionService.getAll()
        call.respond(HttpStatusCode.OK,signReactions.map{it.toSearchSignReactionResponse()})
        logger.info("Ending getting all sign reactions")
    }
}