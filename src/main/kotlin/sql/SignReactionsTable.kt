package sql

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone

object SignReactionsTable : Table("sign_reactions") {
    val id = uuid("id").defaultExpression(genRandomUUID)
    val signId = uuid("sign_id").references(SignsTable.id, onDelete = ReferenceOption.CASCADE)
    val userId = uuid("user_id").references(UsersTable.id)
    val type = signReactionTypeDb("type")
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    override val primaryKey = PrimaryKey(id, name = "sign_reactions_pkey")
    init {
        uniqueIndex("sign_reactions_sign_user_unique", signId, userId)
        index("sign_reactions_sign_id_idx", isUnique = false, signId)
        index("sign_reactions_user_id_idx", isUnique = false, userId)
    }
}
