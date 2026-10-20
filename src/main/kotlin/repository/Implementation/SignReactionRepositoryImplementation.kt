package repository.Implementation

import exception.ResourceNotFoundException
import model.sign_reaction.SignReaction
import model.sign_reaction.SignReactionType
import model.sign_reaction.toSignReactionTypeDb
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
import repository.SignReactionRepositoryInterface
import sql.SignReactionsTable
import sql.toSignReactionType
import kotlin.uuid.Uuid

class SignReactionRepositoryImplementation(
    private val database : Database
) : SignReactionRepositoryInterface {

    private fun JdbcTransaction.insert(
        userId : Uuid,
        signId : Uuid,
        type : SignReactionType
    ) : Uuid{
        return SignReactionsTable.insert {
            it[SignReactionsTable.userId] = userId
            it[SignReactionsTable.signId] = signId
            it[SignReactionsTable.type] = type.toSignReactionTypeDb()
        } get SignReactionsTable.id
    }

    private fun JdbcTransaction.update(
        id : Uuid,
        type : SignReactionType
    ) {
        SignReactionsTable.update(
            where = { SignReactionsTable.id eq id}
        ){
            it[SignReactionsTable.type] = type.toSignReactionTypeDb()
        }
    }

    private fun JdbcTransaction.deleteById(id : Uuid) : Boolean{
        return SignReactionsTable.deleteWhere{
            SignReactionsTable.id eq id
        } > 0
    }

    private fun JdbcTransaction.deleteBySignId(signId: Uuid): Int{
        return SignReactionsTable.deleteWhere {
            SignReactionsTable.signId eq signId
        }
    }

    private fun JdbcTransaction.deleteByUserId(userId: Uuid): Int{
        return SignReactionsTable.deleteWhere {
            SignReactionsTable.userId eq userId
        }
    }

    private fun JdbcTransaction.deleteByUserAndSignId(userId: Uuid, signId: Uuid): Boolean{
        return SignReactionsTable.deleteWhere {
            (SignReactionsTable.signId eq signId) and
                    (SignReactionsTable.userId eq userId)
        } > 0
    }

    private fun JdbcTransaction.findById(id: Uuid): SignReaction?{
        return SignReactionsTable
            .selectAll()
            .where { SignReactionsTable.id eq id }
            .singleOrNull()
            ?.toSignReaction()
    }

    private fun JdbcTransaction.findBySignId(signId: Uuid): List<SignReaction> {
        return SignReactionsTable
            .selectAll()
            .where { SignReactionsTable.signId eq signId }
            .map { it.toSignReaction() }
    }

    private fun JdbcTransaction.findByUserId(userId: Uuid): List<SignReaction> {
        return SignReactionsTable
            .selectAll()
            .where { SignReactionsTable.userId eq userId }
            .map { it.toSignReaction() }
    }

    private fun JdbcTransaction.findByUserAndSignId(
        userId: Uuid,
        signId: Uuid
    ): SignReaction?{
        return SignReactionsTable
            .selectAll()
            .where {
                (SignReactionsTable.userId eq userId) and
                        (SignReactionsTable.signId eq signId)
            }
            .singleOrNull()
            ?.toSignReaction()
    }

    private fun JdbcTransaction.getAll() : List<SignReaction> {
        return SignReactionsTable.selectAll().map{it.toSignReaction()}
    }

    override suspend fun create(
        userId : Uuid,
        signId : Uuid,
        type : SignReactionType
    ) : Uuid{
        return dbQuery{
            insert(
                userId,
                signId,
                type
            )
        }
    }

    override suspend fun updateAndReturn(
        id : Uuid,
        type : SignReactionType
    ) : SignReaction {
        return dbQuery {
            update(
                id,
                type
            )
            findById(id) ?: throw ResourceNotFoundException("signReaction")
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

    override suspend fun deleteByUserAndSignId(userId: Uuid, signId: Uuid): Boolean {
        return dbQuery {
            deleteByUserAndSignId(userId,signId)
        }
    }

    override suspend fun findById(id: Uuid): SignReaction? {
        return dbQuery {
            findById(id)
        }
    }

    override suspend fun findBySignId(signId: Uuid): List<SignReaction> {
        return dbQuery {
            findBySignId(signId)
        }
    }

    override suspend fun findByUserId(userId: Uuid): List<SignReaction> {
        return dbQuery {
            findByUserId(userId)
        }
    }

    override suspend fun findByUserAndSignId(
        userId: Uuid,
        signId: Uuid
    ): SignReaction? {
        return dbQuery {
            findByUserAndSignId(userId,signId)
        }
    }

    override suspend fun getAll() : List<SignReaction>{
        return dbQuery {
            getAll()
        }
    }

    private suspend fun <T> dbQuery(
        block: suspend JdbcTransaction.() -> T
    ): T = suspendTransaction(db = database) {
        block()
    }

    private fun ResultRow.toSignReaction() : SignReaction {
        return SignReaction(
            id = this[SignReactionsTable.id],
            signId = this[SignReactionsTable.signId],
            userId = this[SignReactionsTable.userId],
            type = this[SignReactionsTable.type].toSignReactionType(),
            createdAt = this[SignReactionsTable.createdAt]
        )
    }
}