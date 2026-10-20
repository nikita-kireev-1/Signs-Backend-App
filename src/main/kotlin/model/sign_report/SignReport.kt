package model.sign_report

import java.time.OffsetDateTime
import kotlin.uuid.Uuid

data class SignReport(
    val id : Uuid,
    val signId : Uuid,
    val userId : Uuid,
    val reason : SignReportReason,
    val createdAt : OffsetDateTime
){
    fun toCreateSignReportResponse() : CreateSignReportResponse{
        return CreateSignReportResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            reason = this.reason,
            createdAt = this.createdAt.toString()
        )
    }
    fun toUpdateSignReportResponse() : UpdateSignReportResponse{
        return UpdateSignReportResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            reason = this.reason,
            createdAt = this.createdAt.toString()
        )
    }
    fun toSearchSignReportResponse() : SearchSignReportResponse{
        return SearchSignReportResponse(
            id = this.id.toString(),
            signId = this.signId.toString(),
            userId = this.userId.toString(),
            reason = this.reason,
            createdAt = this.createdAt.toString()
        )
    }
}
