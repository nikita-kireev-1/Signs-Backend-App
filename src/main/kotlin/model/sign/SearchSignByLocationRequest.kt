package model.sign

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class SearchSignByLocationRequest(
    @field:Schema(description = "Latitude", required = true)
    val latitude : Double,
    @field:Schema(description = "Longitude", required = true)
    val longitude : Double
)
