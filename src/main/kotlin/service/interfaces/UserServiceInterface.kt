package service.interfaces

import model.user.User
import kotlin.uuid.Uuid

interface UserServiceInterface {
    suspend fun createUser(
        username : String,
        password : String
    ) : User
    suspend fun updateUser(
        username : String,
        password : String,
        id : Uuid
    ) : User
    suspend fun deleteUserByUUID(
        id : Uuid,
        requesterId : Uuid,
    ) : Boolean
    suspend fun deleteUserByName(
        userId : Uuid,
        username : String
    ) : Boolean
    suspend fun findUserByUUID(
        id : Uuid
    ) : User
    suspend fun findUserByName(
        username : String
    ) : User
    suspend fun getAll() : List<User>
}