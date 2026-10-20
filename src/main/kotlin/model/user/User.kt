package model.user


import java.time.OffsetDateTime
import kotlin.uuid.Uuid


data class User(
    val id : Uuid,
    val username : String,
    val passwordHash : String,
    val createdAt : OffsetDateTime,
){
    fun toCreateUserResponse() : CreateUserResponse{
        return CreateUserResponse(
            id = this.id.toString(),
            username = this.username,
            createdAt = this.createdAt.toString()
        )
    }
    fun toUpdateUserResponse() : UpdateUserResponse {
        return UpdateUserResponse(
            id = this.id.toString(),
            username = this.username,
            createdAt = this.createdAt.toString()
        )
    }
    fun toSearchUserResponse() : SearchUserResponse {
        return SearchUserResponse(
            id = this.id.toString(),
            username = this.username,
            createdAt = this.createdAt.toString()
        )
    }
}
