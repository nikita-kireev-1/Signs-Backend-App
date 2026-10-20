package model.sign_report

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class DeleteSignReportBySignIdRequest(
    @field:Schema(description = "SignId", required = true, format = "uuid")
    val signId : String
)
