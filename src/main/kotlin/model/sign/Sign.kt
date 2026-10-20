package model.sign

import java.time.OffsetDateTime
import java.util.UUID
import kotlin.time.Instant
import kotlin.uuid.Uuid

data class Sign(
    val id: Uuid,
    val authorId: Uuid,
    val latitude: Double,
    val longitude: Double,
    val text: String,
    val status: SignStatus,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
    val expiresAt: OffsetDateTime?
){
    fun toCreateSignResponse() : CreateSignResponse{
        return CreateSignResponse(
            id = this.id.toString(),
            authorId = this.authorId.toString(),
            latitude = this.latitude,
            longitude = this.longitude,
            text = this.text,
            status = this.status,
            createdAt = this.createdAt.toString(),
            expiresAt = this.expiresAt.toString()
        )
    }
    fun toUpdateSignResponse() : UpdateSignResponse{
        return UpdateSignResponse(
            id = this.id.toString(),
            authorId = this.authorId.toString(),
            latitude = this.latitude,
            longitude = this.longitude,
            text = this.text,
            status = this.status,
            createdAt = this.createdAt.toString(),
            updatedAt = this.updatedAt.toString(),
            expiresAt = this.expiresAt.toString()
        )
    }
    fun toSearchSignResponse() : SearchSignResponse{
        return SearchSignResponse(
            id = this.id.toString(),
            authorId = this.authorId.toString(),
            latitude = this.latitude,
            longitude = this.longitude,
            text = this.text,
            status = this.status,
            createdAt = this.createdAt.toString(),
            updatedAt = this.updatedAt.toString(),
            expiresAt = this.expiresAt.toString()
        )
    }
}
