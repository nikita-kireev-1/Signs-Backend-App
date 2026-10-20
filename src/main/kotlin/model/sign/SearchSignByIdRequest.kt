package model.sign

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class SearchSignByIdRequest(
    @field:Schema(description = "Id", required = true, format = "uuid")
    val id : String
)