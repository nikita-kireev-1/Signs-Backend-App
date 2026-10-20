package model.sign

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.time.Instant

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class CreateSignRequest(
    @field:Schema(description = "Latitude", required = true)
    val latitude: Double,
    @field:Schema(description = "Longitude", required = true)
    val longitude: Double,
    @field:Schema(description = "Text", required = true)
    val text: String,
    @field:Schema(description = "ExpiresAt", required = true)
    val expiresAt: String
)
