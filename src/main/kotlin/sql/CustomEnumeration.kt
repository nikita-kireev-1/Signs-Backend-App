package sql

import model.sign.SignStatus
import model.sign_reaction.SignReactionType
import model.sign_report.SignReportReason

enum class SignStatusDb(val dbValue: String) {
    ACTIVE("active"),
    HIDDEN("hidden"),
    EXPIRED("expired"),
    DELETED("deleted");
    companion object {
        fun fromDb(v: String) = entries.first { it.dbValue == v }
    }
}
fun SignStatusDb.toSignStatus() : SignStatus{
    return when(this){
        SignStatusDb.ACTIVE -> SignStatus.ACTIVE
        SignStatusDb.HIDDEN -> SignStatus.HIDDEN
        SignStatusDb.EXPIRED -> SignStatus.EXPIRED
        SignStatusDb.DELETED -> SignStatus.DELETED
    }
}

enum class SignReactionTypeDb(val dbValue: String) {
    LIKE("like"),
    DISLIKE("dislike");
    companion object {
        fun fromDb(v: String) = entries.first { it.dbValue == v }
    }
}
fun SignReactionTypeDb.toSignReactionType() : SignReactionType{
    return when(this){
        SignReactionTypeDb.LIKE -> SignReactionType.LIKE
        SignReactionTypeDb.DISLIKE -> SignReactionType.DISLIKE
    }
}

enum class SignReportReasonDb(val dbValue: String) {
    SPAM("spam"),
    ABUSE("abuse"),
    INAPPROPRIATE("inappropriate"),
    FALSE_INFO("false_info"),
    OTHER("other");
    companion object {
        fun fromDb(v: String) = entries.first { it.dbValue == v }
    }
}
fun SignReportReasonDb.toSignReportReason() : SignReportReason {
    return when(this){
        SignReportReasonDb.SPAM -> SignReportReason.SPAM
        SignReportReasonDb.ABUSE -> SignReportReason.ABUSE
        SignReportReasonDb.INAPPROPRIATE -> SignReportReason.INAPPROPRIATE
        SignReportReasonDb.FALSE_INFO -> SignReportReason.FALSE_INFO
        SignReportReasonDb.OTHER -> SignReportReason.OTHER
    }
}