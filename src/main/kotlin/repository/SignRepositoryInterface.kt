package repository

import model.sign.Sign
import model.sign.SignStatus
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

interface SignRepositoryInterface {
    suspend fun create(
        authorId : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Uuid
    suspend fun updateAndReturn(
        id : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Sign
    suspend fun getAll() : List<Sign>
    suspend fun deleteHighlyReportedSigns(minReports: Int) : Int
    suspend fun deleteExpired() : Int
    suspend fun deleteById(
        id : Uuid
    ) : Boolean
    suspend fun deleteByAuthorId(
        authorId : Uuid
    ) : Int
    suspend fun searchById(
        id : Uuid
    ) : Sign?
    suspend fun searchByAuthorId(
        authorId : Uuid
    ) : List<Sign>
    suspend fun searchByLocation(
        longitude : Double,
        latitude : Double
    ) :List<Sign>
}