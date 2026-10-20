package model.sign_reaction

import java.time.OffsetDateTime
import kotlin.uuid.Uuid

data class SignReaction(
    val id : Uuid,
    val signId : Uuid,
    val userId : Uuid,
    val type : SignReactionType,
    val createdAt : OffsetDateTime,
){
    fun toCreateSignReactionResponse() : CreateSignReactionResponse{
        return CreateSignReactionResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            type = this.type,
            createdAt = this.createdAt.toString()
        )
    }
    fun toUpdateSignReactionResponse() : UpdateSignReactionResponse{
        return UpdateSignReactionResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            type = this.type,
            createdAt = this.createdAt.toString()
        )
    }
    fun toSearchSignReactionResponse() : SearchSignReactionResponse{
        return SearchSignReactionResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            type = this.type,
            createdAt = this.createdAt.toString()
        )
    }
}
