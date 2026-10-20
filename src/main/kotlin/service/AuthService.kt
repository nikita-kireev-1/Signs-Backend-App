package service

import exception.InvalidCredentialsException
import exception.UsernameAlreadyExistsException
import exception.WrongCredentialsException
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import repository.UserRepositoryInterface
import service.interfaces.AuthServiceInterface
import service.interfaces.JWTServiceInterface
import service.interfaces.UserServiceInterface

class AuthService(
    private val userRepository: UserRepositoryInterface,
    private val userService : UserServiceInterface,
    private val jwtService: JWTServiceInterface
) : AuthServiceInterface {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)
    override suspend fun login(
        username: String,
        password: String
    ): String {
        logger.info("Starting to check login arguments")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$")) || password.length !in 8..16 || !password.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid $password or $username for login",password,username )
            throw InvalidCredentialsException()
        }
        val user = userService.findUserByName(username)
        if(BCrypt.checkpw(password, user.passwordHash)){
            return jwtService.generateToken(
                userId = user.id.toString()
            )
        }else{
            logger.warn("Wrong $password",password)
            throw WrongCredentialsException()
        }

    }
    override suspend fun register(
        username : String,
        password : String,
    ) : String {
        logger.info("Starting to check register arguments")
        if(username.length !in 4..20 || !username.matches(Regex("^[A-Za-z0-9_]+$")) || password.length !in 8..16 || !password.matches(Regex("^[A-Za-z0-9_]+$"))){
            logger.warn("Invalid $password or $username for register",password,username)
            throw InvalidCredentialsException()
        }
        if( userRepository.findByName(username) != null){
            logger.warn("$username already exists",username)
            throw UsernameAlreadyExistsException()
        }
        userService.createUser(
            username,
            password
        )
        return login(
            username,
            password
        )
    }
}