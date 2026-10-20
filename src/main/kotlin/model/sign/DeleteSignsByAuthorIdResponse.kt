package model.sign

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class DeleteSignsByAuthorIdResponse(
    @field:Schema(description = "Counter", required = true)
    val counter : Int
)
