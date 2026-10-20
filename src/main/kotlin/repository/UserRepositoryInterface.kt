package repository

import model.user.User
import kotlin.uuid.Uuid

interface UserRepositoryInterface {
    suspend fun createUser(
        username : String,
        passwordHash : String
    ) : Uuid
    suspend fun updateAndReturn(
        user : User
    ) : User
    suspend fun deleteByName(
        username : String
    ) : Boolean
    suspend fun deleteByUUID(
        id : Uuid
    ) : Boolean
    suspend fun findByUUID(
        id : Uuid
    ) : User?
    suspend fun findByName(
        username : String
    ) : User?
    suspend fun getAll() : List<User>
}