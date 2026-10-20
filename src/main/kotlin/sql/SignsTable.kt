package sql

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.javatime.CurrentTimestampWithTimeZone
import org.jetbrains.exposed.v1.javatime.timestampWithTimeZone

object SignsTable : Table("signs") {
    val id = uuid("id").defaultExpression(genRandomUUID)
    val authorId = uuid("author_id").references(UsersTable.id, onDelete = ReferenceOption.NO_ACTION)
    val latitude = double("latitude")
    val longitude = double("longitude")
    val text = text("text")
    val status = signStatusDb("status").default(SignStatusDb.ACTIVE)
    val createdAt = timestampWithTimeZone("created_at").defaultExpression(CurrentTimestampWithTimeZone)
    val updatedAt = timestampWithTimeZone("updated_at").defaultExpression(CurrentTimestampWithTimeZone)
    val expiresAt = timestampWithTimeZone("expires_at").nullable()
    override val primaryKey = PrimaryKey(id, name = "signs_pkey")
    init {
        check("signs_latitude_check") { latitude.between(-90.0, 90.0) }
        check("signs_longitude_check") { longitude.between(-180.0, 180.0) }
        index("signs_author_id_idx", isUnique = false, authorId)
        index("signs_status_idx", isUnique = false, status)
        index("signs_expires_at_idx", isUnique = false, expiresAt)
    }
}