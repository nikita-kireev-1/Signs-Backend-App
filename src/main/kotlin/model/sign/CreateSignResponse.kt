package model.sign

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.UUID
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class CreateSignResponse(
    @field:Schema(description = "Id", required = true, format = "uuid")
    val id: String,
    @field:Schema(description = "AuthorId", required = true, format = "uuid")
    val authorId: String,
    @field:Schema(description = "Latitude", required = true)
    val latitude: Double,
    @field:Schema(description = "Longitude", required = true)
    val longitude: Double,
    @field:Schema(description = "Text", required = true)
    val text: String,
    @field:Schema(description = "Status", required = true)
    val status: SignStatus,
    @field:Schema(description = "CreatedAt", required = true, format = "date-time",)
    val createdAt: String,
    @field:Schema(description = "ExpiresAt", required = true, format = "date-time",)
    val expiresAt: String?

)
