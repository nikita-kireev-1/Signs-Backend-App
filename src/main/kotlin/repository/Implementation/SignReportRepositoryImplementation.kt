package repository.Implementation

import exception.ResourceNotFoundException
import model.sign_report.SignReport
import model.sign_report.SignReportReason
import model.sign_report.toSignReportDb
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
import repository.SignReportRepositoryInterface
import sql.SignReportsTable
import sql.toSignReportReason
import kotlin.uuid.Uuid

class SignReportRepositoryImplementation(
    private val database : Database
) : SignReportRepositoryInterface {

    private fun JdbcTransaction.insert(
        userId : Uuid,
        signId : Uuid,
        reason : SignReportReason
    ) : Uuid{
        return SignReportsTable.insert{
            it[SignReportsTable.userId] = userId
            it[SignReportsTable.signId] = signId
            it[SignReportsTable.reason] = reason.toSignReportDb()
        } get SignReportsTable.id
    }

    private fun JdbcTransaction.update(
        id : Uuid,
        reason : SignReportReason
    ) {
        SignReportsTable.update(
            where = { SignReportsTable.id eq id}
        ){
            it[SignReportsTable.reason] = reason.toSignReportDb()
        }
    }

    private fun JdbcTransaction.deleteById(
        id: Uuid
    ): Boolean {
        return SignReportsTable.deleteWhere {
            SignReportsTable.id eq id
        } > 0
    }

    private fun JdbcTransaction.deleteBySignId(
        signId: Uuid
    ) : Int {
        return SignReportsTable.deleteWhere {
            SignReportsTable.signId eq signId
        }
    }

    private fun JdbcTransaction.deleteByUserId(
        userId: Uuid
    ) : Int{
        return SignReportsTable.deleteWhere {
            SignReportsTable.userId eq userId
        }
    }

    private fun JdbcTransaction.deleteBySignAndUserId(
        signId: Uuid,
        userId: Uuid
    ) : Boolean {
        return SignReportsTable.deleteWhere {
            (SignReportsTable.signId eq signId) and
                    (SignReportsTable.userId eq userId)
        } > 0
    }

    private fun JdbcTransaction.findById(id: Uuid): SignReport? {
        return SignReportsTable
            .selectAll()
            .where { SignReportsTable.id eq id }
            .singleOrNull()
            ?.toSignReport()
    }

    private fun JdbcTransaction.findBySignId(signId: Uuid): List<SignReport> {
        return SignReportsTable
            .selectAll()
            .where { SignReportsTable.signId eq signId }
            .map { it.toSignReport() }
    }

    private fun JdbcTransaction.findByUserId(userId: Uuid): List<SignReport>{
        return SignReportsTable
            .selectAll()
            .where { SignReportsTable.userId eq userId }
            .map { it.toSignReport() }
    }

    private fun JdbcTransaction.findBySignAndUserId(
        signId: Uuid,
        userId: Uuid
    ): SignReport?{
        return SignReportsTable
            .selectAll()
            .where {
                (SignReportsTable.signId eq signId) and
                        (SignReportsTable.userId eq userId)
            }
            .singleOrNull()
            ?.toSignReport()
    }

    private fun JdbcTransaction.getAll() : List<SignReport>{
        return SignReportsTable.selectAll().map{it.toSignReport()}
    }

    override suspend fun create(
        userId : Uuid,
        signId : Uuid,
        reason : SignReportReason
    ) : Uuid{
        return dbQuery {
            insert(
                userId,
                signId,
                reason
            )
        }
    }

    override suspend fun updateAndReturn(
        id : Uuid,
        reason : SignReportReason
    ) : SignReport{
        return dbQuery {
            update(
                id,
                reason
            )
            findById(id) ?: throw ResourceNotFoundException("updated signReport")
        }
    }
    override suspend fun getAll() : List<SignReport>{
        return dbQuery{
            getAll()
        }
    }

    override suspend fun deleteById(id: Uuid): Boolean {
        return dbQuery{
            deleteById(id)
        }
    }

    override suspend fun deleteBySignId(signId: Uuid): Int {
        return dbQuery{
            deleteBySignId(signId)
        }
    }

    override suspend fun deleteByUserId(userId: Uuid): Int {
        return dbQuery{
            deleteByUserId(userId)
        }
    }

    override suspend fun deleteBySignAndUserId(signId: Uuid, userId: Uuid): Boolean {
        return dbQuery{
            deleteBySignAndUserId(signId, userId)
        }
    }

    override suspend fun findById(id: Uuid): SignReport? {
        return dbQuery {
            findById(id)
        }
    }

    override suspend fun findBySignId(signId: Uuid): List<SignReport> {
        return dbQuery {
            findBySignId(signId)
        }
    }

    override suspend fun findByUserId(userId: Uuid): List<SignReport> {
        return dbQuery {
            findByUserId(userId)
        }
    }

    override suspend fun findBySignAndUserId(
        signId: Uuid,
        userId: Uuid
    ): SignReport? {
        return dbQuery {
            findBySignAndUserId(signId,userId)
        }
    }

    private suspend fun <T> dbQuery(
        block: suspend JdbcTransaction.() -> T
    ): T = suspendTransaction(db = database) {
        block()
    }

    private fun ResultRow.toSignReport() : SignReport{
        return SignReport(
            id = this[SignReportsTable.id],
            signId = this[SignReportsTable.signId],
            userId = this[SignReportsTable.userId],
            reason = this[SignReportsTable.reason].toSignReportReason(),
            createdAt = this[SignReportsTable.createdAt]
        )
    }
}