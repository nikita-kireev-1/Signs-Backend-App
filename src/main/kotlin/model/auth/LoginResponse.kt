package model.auth

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class LoginResponse(
    @field:Schema(description = "Token",required = true)
    val accessToken: String
)
