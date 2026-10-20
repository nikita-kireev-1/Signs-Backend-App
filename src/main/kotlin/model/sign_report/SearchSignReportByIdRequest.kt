package model.sign_report

import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import kotlin.uuid.Uuid

@Serializable
@Schema(additionalProperties = Schema.AdditionalPropertiesValue.FALSE)
data class SearchSignReportByIdRequest(
    @field:Schema(description = "Id", required = true, format = "uuid")
    val id : String,
)
