package model.user

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class UpdateUserResponse(
    @field:Schema(description = "Id", required = true, format = "uuid")
    val id : String,
    @field:Schema(description = "Username", required = true)
    val username : String,
    @field:Schema(description = "CreatedAt", required = true, format = "date-time",)
    val createdAt : String,
)
