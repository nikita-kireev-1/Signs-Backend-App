package model.sign_reaction

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class DeleteSignReactionResponse(
    @field:Schema(description = "Counter", required = true)
    val counter : Int,
)
