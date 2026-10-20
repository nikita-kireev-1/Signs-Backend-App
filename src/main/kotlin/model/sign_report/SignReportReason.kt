package model.sign_report

import sql.SignReportReasonDb

enum class SignReportReason {
    SPAM,
    ABUSE,
    INAPPROPRIATE,
    FALSE_INFO,
    OTHER
}
fun SignReportReason.toSignReportDb() : SignReportReasonDb{
    return when(this){
        SignReportReason.SPAM -> SignReportReasonDb.SPAM
        SignReportReason.ABUSE -> SignReportReasonDb.ABUSE
        SignReportReason.INAPPROPRIATE -> SignReportReasonDb.INAPPROPRIATE
        SignReportReason.FALSE_INFO -> SignReportReasonDb.FALSE_INFO
        SignReportReason.OTHER -> SignReportReasonDb.OTHER
    }
}