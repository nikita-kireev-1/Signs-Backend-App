package sql

import org.jetbrains.exposed.v1.core.CustomFunction
import org.jetbrains.exposed.v1.core.UuidColumnType
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import kotlin.uuid.Uuid

val genRandomUUID: CustomFunction<Uuid> =
    CustomFunction("gen_random_uuid", UuidColumnType())

fun Table.signStatusDb(name: String): Column<SignStatusDb> =
    customEnumeration(
        name = name,
        sql = "sign_status",
        fromDb = { SignStatusDb.fromDb(it as String) },
        toDb = { it.dbValue }
    )

fun Table.signReactionTypeDb(name: String): Column<SignReactionTypeDb> =
    customEnumeration(
        name = name,
        sql = "sign_reaction_type",
        fromDb = { SignReactionTypeDb.fromDb(it as String) },
        toDb = { it.dbValue }
    )

fun Table.signReportReasonDb(name: String): Column<SignReportReasonDb> =
    customEnumeration(
        name = name,
        sql = "sign_report_reason",
        fromDb = { SignReportReasonDb.fromDb(it as String) },
        toDb = { it.dbValue }
    )