package model.auth

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable


@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class LoginRequest(
    @field:Schema(description = "Username", required = true, minLength = 4, maxLength = 20, pattern = "^[A-Za-z0-9_]+$")
    val username: String,
    @field:Schema(description = "Password", required = true, minLength = 8, maxLength = 16, pattern = "^[A-Za-z0-9_]+$")
    val password: String
)
