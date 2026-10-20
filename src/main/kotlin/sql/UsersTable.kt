package sql

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.javatime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone


object UsersTable : Table("users") {
    val id = uuid("id").defaultExpression(genRandomUUID)
    val username = varchar("username", 64)
    val passwordHash = text("password_hash")
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    override val primaryKey = PrimaryKey(id, name = "users_pkey")
    init {
        uniqueIndex("users_username_unique", username)
    }
}