package sql

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone

object SignReportsTable : Table("sign_reports") {
    val id = uuid("id").defaultExpression(genRandomUUID)
    val signId = uuid("sign_id").references(SignsTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = uuid("user_id").references(UsersTable.id)
    val reason = signReportReasonDb("reason")
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    override val primaryKey = PrimaryKey(id, name = "sign_reports_pkey")
    init {
        index("sign_reports_sign_id_idx", isUnique = false, signId)
        index("sign_reports_user_id_idx", isUnique = false, userId)
    }
}