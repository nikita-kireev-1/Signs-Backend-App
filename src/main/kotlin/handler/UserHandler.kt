package handler

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import model.user.CreateUserRequest
import model.user.DeleteUserByIdRequest
import model.user.DeleteUserByUsernameRequest
import model.user.SearchUserByIdRequest
import model.user.SearchUserByUsernameRequest
import model.user.UpdateUserRequest
import org.slf4j.LoggerFactory
import service.UserService
import service.interfaces.UserServiceInterface
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserHandler(
    private val userService : UserServiceInterface
) {
    private val logger = LoggerFactory.getLogger(UserHandler::class.java)
    suspend fun createUser(
        call : ApplicationCall
    ){
        logger.info("Starting to create user")
        val request = call.receive<CreateUserRequest>()
        val user = userService.createUser(
            request.username,
            request.password
        )
        call.respond(HttpStatusCode.Created,user.toCreateUserResponse())
        logger.info("Ending to create user")
    }
    suspend fun updateUser(
        call : ApplicationCall
    ){
        logger.info("Starting to update user")
        val request = call.receive<UpdateUserRequest>()
        val userId = call.requireUserId()
        val user = userService.updateUser(
            username = request.username,
            password = request.password,
            id = userId
        )
        call.respond(HttpStatusCode.OK,user.toUpdateUserResponse())
        logger.info("Ending to update user")
    }
    suspend fun deleteUserById(
        call : ApplicationCall
    ){
        logger.info("Starting to delete user by id")
        val request = call.receive<DeleteUserByIdRequest>()
        val deleteConfirmed = userService.deleteUserByUUID(
            id = Uuid.parse(request.id),
            requesterId = call.requireUserId()
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete user by id")
        }else {
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete user by id")
        }
    }
    suspend fun deleteUserByUsername(
        call : ApplicationCall
    ){
        logger.info("Starting to delete user by username")
        val request = call.receive<DeleteUserByUsernameRequest>()
        val userId = call.requireUserId()
        val deleteConfirmed = userService.deleteUserByName(
            userId = userId,
            username = request.username
        )
        if(deleteConfirmed){
            call.respond(HttpStatusCode.OK)
            logger.info("Ending to delete user by username")
        }else{
            call.respond(HttpStatusCode.NoContent)
            logger.warn("Failed to delete user by username")
        }
    }
    suspend fun searchUserById(
        call : ApplicationCall
    ) {
        logger.info("Starting to search for a user by id")
        val request = call.receive<SearchUserByIdRequest>()
        val user = userService.findUserByUUID(
            Uuid.parse(request.id)
        )
        call.respond(HttpStatusCode.OK,user.toSearchUserResponse())
        logger.info("Ending to search for a user by id")
    }
    suspend fun searchUserByUsername(
        call: ApplicationCall
    ) {
        logger.info("Starting to search for a user by username")
        val request = call.receive<SearchUserByUsernameRequest>()
        val user = userService.findUserByName(
            request.username
        )
        call.respond(HttpStatusCode.OK, user.toSearchUserResponse())
        logger.info("Ending to search for a user by username")
    }
    suspend fun getAll(
        call : ApplicationCall
    ) {
       logger.info("Starting to get all users")
       val users = userService.getAll()
       call.respond(HttpStatusCode.OK, users.map{it.toSearchUserResponse()})
       logger.info("Ending to get all users")
    }
}