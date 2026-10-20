package model.sign_report

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class CreateSignReportResponse(
    @field:Schema(description = "Id", required = true, format = "uuid")
    val id : String,
    @field:Schema(description = "SignId", required = true, format = "uuid")
    val signId : String,
    @field:Schema(description = "UserId", required = true, format = "uuid")
    val userId : String,
    @field:Schema(description = "Reason", required = true)
    val reason : SignReportReason,
    @field:Schema(description = "CreatedAt", required = true, format = "date-time",)
    val createdAt : String,
)