package service.interfaces

import model.sign_reaction.SignReaction
import model.sign_reaction.SignReactionType
import model.sign_report.SignReportReason
import kotlin.uuid.Uuid

interface SignReactionServiceInterface {
    suspend fun create(
        userId : Uuid,
        signId : Uuid,
        type : SignReactionType
    ) : SignReaction
    suspend fun update(
        id : Uuid,
        userId : Uuid,
        type : SignReactionType
    ) : SignReaction
    suspend fun deleteById(
        id : Uuid,
        userId : Uuid,
    ) : Boolean
    suspend fun deleteBySignId(
        userId : Uuid,
        signId : Uuid
    ) : Int
    suspend fun deleteByUserId(
        requesterId : Uuid,
        userId : Uuid
    ) : Int
    suspend fun deleteByUserAndSignId(
        requesterId : Uuid,
        userId : Uuid,
        signId : Uuid
    ) : Boolean
    suspend fun findById(
        id : Uuid
    ) : SignReaction
    suspend fun findByUserId(
        userId : Uuid
    ) : List<SignReaction>
    suspend fun findBySignId(
        signId : Uuid
    ) : List<SignReaction>
    suspend fun findBySignAndUserId(
        userId : Uuid,
        signId : Uuid
    ) : SignReaction
    suspend fun getAll() : List<SignReaction>
}