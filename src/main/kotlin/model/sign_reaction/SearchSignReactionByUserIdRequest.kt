package model.sign_reaction

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class SearchSignReactionByUserIdRequest(
    @field:Schema(description = "UserId", required = true, format = "uuid")
    val userId : String,
)
