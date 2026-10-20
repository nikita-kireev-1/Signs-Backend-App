package model.user

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class CreateUserRequest(
    @field:Schema(description = "Username", required = true, pattern = "^[A-Za-z0-9_]+$")
    val username : String,
    @field:Schema(description = "Password", required = true)
    val password : String,
)
