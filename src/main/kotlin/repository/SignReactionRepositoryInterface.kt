package repository

import model.sign_reaction.SignReaction
import model.sign_reaction.SignReactionType
import kotlin.uuid.Uuid

interface SignReactionRepositoryInterface {
    suspend fun create(
        userId : Uuid,
        signId : Uuid,
        type : SignReactionType
    ) : Uuid
    suspend fun updateAndReturn(
        id : Uuid,
        type : SignReactionType
    ) : SignReaction
    suspend fun getAll() : List<SignReaction>
    suspend fun deleteById(
        id : Uuid
    ) : Boolean
    suspend fun deleteBySignId(
        signId : Uuid
    ) : Int
    suspend fun deleteByUserId(
        userId : Uuid
    ) : Int
    suspend fun deleteByUserAndSignId(
        userId : Uuid,
        signId : Uuid
    ) : Boolean
    suspend fun findById(
        id : Uuid
    ) : SignReaction?
    suspend fun findBySignId(
        signId : Uuid
    ) : List<SignReaction>
    suspend fun findByUserId(
        userId : Uuid
    ) : List<SignReaction>
    suspend fun findByUserAndSignId(
        userId : Uuid,
        signId : Uuid
    ) : SignReaction?
}