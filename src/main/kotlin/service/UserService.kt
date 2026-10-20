package service

import exception.ForbiddenException
import exception.InvalidCredentialsException
import exception.ResourceNotFoundException
import exception.UsernameAlreadyExistsException
import exception.ValidationException
import model.user.User
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import repository.UserRepositoryInterface
import service.interfaces.UserServiceInterface
import kotlin.uuid.Uuid

class UserService(
    private val userRepository : UserRepositoryInterface
) : UserServiceInterface {
    private val logger = LoggerFactory.getLogger(UserService::class.java)
    override suspend fun createUser(username: String, password: String): User {
        logger.info("Starting to check arguments to create a user")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$")) || password.length !in 8..16 || !password.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid arguments to create a user")
            throw ValidationException(mapOf(Pair("createUser","username,password not valid")))
        }
        val passwordHash = BCrypt.hashpw(
            password,
            BCrypt.gensalt()
        )
        if( userRepository.findByName(username) != null){
            logger.warn("User with such username already exist")
            throw UsernameAlreadyExistsException()
        }
        val uuid = userRepository.createUser(username,passwordHash)
        val user = userRepository.findByUUID(uuid)
        if(user == null){
            logger.warn("User to create not found")
            throw ResourceNotFoundException("Created user")
        }else{
            return user
        }
    }

    override suspend fun updateUser(
        username: String,
        password: String,
        id: Uuid
    ): User {
        logger.info("Starting to check arguments to update a user")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$")) || password.length !in 8..16 || !password.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid arguments to update a user")
            throw ValidationException(mapOf(Pair("updateUser","username,password not valid")))
        }
        val user = userRepository.findByUUID(id)
        if(user == null){
            logger.warn("User to update does not exist")
            throw ResourceNotFoundException("user to update")
        }
        if(id != user.id){
            logger.warn("Requester not allowed to update a user")
            throw ForbiddenException()
        }
        val usernameCheck = userRepository.findByName(username)
        if( usernameCheck != null && usernameCheck.id != user.id ){
            logger.warn("Username to update for current user already exists")
            throw UsernameAlreadyExistsException()
        }
        val passwordHash = BCrypt.hashpw(
            password,
            BCrypt.gensalt()
        )
        val updatedUser = user.copy( username = username, passwordHash = passwordHash)
        return userRepository.updateAndReturn(updatedUser)
    }

    override suspend fun deleteUserByUUID(id: Uuid): Boolean {
        logger.info("Starting to check arguments to delete user by Uuid")
        val user = userRepository.findByUUID(id)
        if(user == null){
            logger.warn("User to delete do not exist")
            throw ResourceNotFoundException("user to delete")
        }
        if(user.id != id){
            logger.warn("Requester is not allowed to delete a user")
            throw ForbiddenException()
        }
        val deletionConfirmed = userRepository.deleteByUUID(id)
        return deletionConfirmed
    }

    override suspend fun deleteUserByName(userId : Uuid, username: String): Boolean {
        logger.info("Starting to check arguments to delete a user by username")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid name to delete a user")
            throw ValidationException(mapOf(Pair("deleteUserByName","username not valid")))
        }
        val user = userRepository.findByName(username)
        if(user == null){
            logger.warn("user to delete do not exist")
            throw ResourceNotFoundException("user to delete")
        }
        if(user.id != userId){
            logger.warn("Requester is not allowed to delete a user by name")
            throw ForbiddenException()
        }
        val deletionConfirmed = userRepository.deleteByName(username)
        return deletionConfirmed
    }

    override suspend fun findUserByUUID(id: Uuid): User {
        logger.info("Starting to check arguments to find a user by Uuid")
        val user = userRepository.findByUUID(id)
        if(user == null){
            logger.warn("User to find does not exist")
            throw ResourceNotFoundException("user to find")
        }else{
            return user
        }
    }

    override suspend fun findUserByName(username: String): User {
        logger.info("Starting to check arguments to find a user by name")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid name to find a user")
            throw ValidationException(mapOf(Pair("findUserByName","username not valid")))
        }
        val user = userRepository.findByName(username)
        if(user == null){
            logger.warn("User to find by name do not exist")
            throw ResourceNotFoundException("user to find")
        }else{
            return user
        }
    }

    override suspend fun getAll() : List<User> {
//        logger.info("Starting to check if users exist")
        val users = userRepository.getAll()
//        if(users == emptyList<User>()){
//            logger.warn("Users to find do not exist")
//            throw ResourceNotFoundException("users to find")
//        }
        return users
    }
}