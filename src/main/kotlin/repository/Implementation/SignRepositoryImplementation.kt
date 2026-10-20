package repository.Implementation

import exception.ResourceNotFoundException
import model.sign.Sign
import model.sign.SignStatus
import model.sign.toSignStatusDb
import org.jetbrains.exposed.v1.core.Count
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.inSubQuery
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.select
import repository.SignRepositoryInterface
import sql.SignReportsTable
import sql.SignsTable
import sql.toSignStatus
import java.time.OffsetDateTime
import java.time.ZoneOffset
import kotlin.uuid.Uuid

class SignRepositoryImplementation(
    private val database : Database
) : SignRepositoryInterface {

    private fun JdbcTransaction.insert(
        authorId : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Uuid {
        return SignsTable.insert {
            it[SignsTable.authorId] = authorId
            it[SignsTable.text] = text
            it[SignsTable.status] = status.toSignStatusDb()
            it[SignsTable.longitude] = longitude
            it[SignsTable.latitude] = latitude
            it[SignsTable.expiresAt] = expiresAt
        } get SignsTable.id
    }

    private fun JdbcTransaction.update(
        id : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) {
        SignsTable.update(
            where = { SignsTable.id eq id }
        ){
            it[SignsTable.text] = text
            it[SignsTable.status] = status.toSignStatusDb()
            it[SignsTable.longitude] = longitude
            it[SignsTable.latitude] = latitude
            it[SignsTable.expiresAt] = expiresAt
        }
    }

    private fun JdbcTransaction.deleteById(
        id: Uuid
    ) : Boolean {
        return SignsTable.deleteWhere{
            SignsTable.id eq id
        } > 0
    }

    private fun JdbcTransaction.deleteByAuthorId(
        authorId: Uuid
    ) : Int {
        return SignsTable.deleteWhere {
            SignsTable.authorId eq authorId
        }
    }

    private fun JdbcTransaction.deleteExpired(

    ) : Int {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        return SignsTable.deleteWhere{
            SignsTable.expiresAt less now
        }
    }

    private fun JdbcTransaction.searchById(
        id : Uuid
    ) : Sign? {
        return SignsTable.selectAll()
            .where { SignsTable.id eq id }
            .singleOrNull()
            ?.toSign()
    }

    private fun JdbcTransaction.searchByAuthorId(
        authorId : Uuid
    ) : List<Sign>{
        return SignsTable.selectAll()
            .where { SignsTable.authorId eq authorId }
            .map { it.toSign() }
    }

    private fun JdbcTransaction.searchByLocation(
        longitude: Double,
        latitude: Double
    ) : List<Sign> {
        return SignsTable.selectAll()
            .where {
                SignsTable.longitude.between(longitude - 0.005, longitude + 0.005) and
                        SignsTable.latitude.between(latitude - 0.005, latitude + 0.005)
            }
            .map { it.toSign() }
    }

    private fun JdbcTransaction.getAll() : List<Sign>{
        return SignsTable.selectAll().map{it.toSign()}
    }

    private fun JdbcTransaction.deleteHighlyReportedSigns(minReports : Int) : Int{
        val reportedSignIds = SignReportsTable
            .select(SignReportsTable.id)
            .groupBy(SignReportsTable.id)
            .having{
                Count(SignReportsTable.id) greater minReports.toLong()
            }
        val deletedCounter = SignsTable.deleteWhere {
            SignsTable.id inSubQuery reportedSignIds
        }
        return deletedCounter
    }

    override suspend fun create(
        authorId : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Uuid{
        return dbQuery{
            insert(
                authorId,
                text,
                status,
                longitude,
                latitude,
                expiresAt
            )
        }
    }

    override suspend fun updateAndReturn(
        id : Uuid,
        text : String,
        status : SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ) : Sign {
        return dbQuery{
            update(
                id,
                text,
                status,
                longitude,
                latitude,
                expiresAt
            )
            searchById(id) ?: throw ResourceNotFoundException("sign updated")
        }
    }

    override suspend fun getAll() : List<Sign>{
        return dbQuery{
            getAll()
        }
    }

    override suspend fun deleteHighlyReportedSigns(minReports: Int) : Int {
        return dbQuery{
            deleteHighlyReportedSigns(
                minReports
            )
        }
    }

    override suspend fun deleteExpired() : Int{
        return dbQuery{
            deleteExpired()
        }
    }

    override suspend fun deleteById(id: Uuid): Boolean {
        return dbQuery{
            deleteById(id)
        }
    }

    override suspend fun deleteByAuthorId(authorId: Uuid): Int {
        return dbQuery{
            deleteByAuthorId(authorId)
        }
    }

    override suspend fun searchById(id: Uuid): Sign? {
        return dbQuery {
           searchById(id)
        }
    }

    override suspend fun searchByAuthorId(authorId: Uuid): List<Sign> {
        return dbQuery {
            searchByAuthorId(authorId)
        }
    }

    override suspend fun searchByLocation(
        longitude: Double,
        latitude: Double
    ): List<Sign> {
        return dbQuery {
            searchByLocation(
                longitude,
                latitude
            )
        }
    }

    private suspend fun <T> dbQuery(
        block: suspend JdbcTransaction.() -> T
    ): T = suspendTransaction(db = database) {
        block()
    }

    private fun ResultRow.toSign() : Sign{
        return Sign(
            id = this[SignsTable.id],
            authorId = this[SignsTable.authorId],
            latitude = this[SignsTable.latitude],
            longitude = this[SignsTable.longitude],
            text = this[SignsTable.text],
            status = this[SignsTable.status].toSignStatus(),
            createdAt = this[SignsTable.createdAt],
            updatedAt = this[SignsTable.updatedAt],
            expiresAt = this[SignsTable.expiresAt]
        )
    }

}