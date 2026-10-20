package service.interfaces

interface AuthServiceInterface {
    suspend fun login(
        username : String,
        password : String,
    ) : String
    suspend fun register(
        username : String,
        password : String,
    ) : String
}