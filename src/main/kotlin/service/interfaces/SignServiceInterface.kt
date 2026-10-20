package service.interfaces

import model.sign.Sign
import model.sign.SignStatus
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

interface SignServiceInterface {
    suspend fun create(
        userId : Uuid,
        authorId : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Sign
    suspend fun update(
        userId : Uuid,
        id : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Sign
    suspend fun getAll() : List<Sign>
    suspend fun deleteExpired() : Int
    suspend fun deleteById(
        id : Uuid,
        userId : Uuid
    ) : Boolean
    suspend fun deleteByAuthorId(
        userId : Uuid,
        authorId : Uuid
    ) : Int
    suspend fun searchById(
        id : Uuid
    ) : Sign
    suspend fun searchByAuthorId(
        authorId : Uuid
    ) : List<Sign>
    suspend fun searchByLocation(
        longitude : Double,
        latitude : Double
    ) : List<Sign>
}