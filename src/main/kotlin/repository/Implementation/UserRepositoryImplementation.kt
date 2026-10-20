package repository.Implementation

import exception.ResourceNotFoundException
import model.user.User
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.update
import repository.UserRepositoryInterface
import sql.UsersTable
import kotlin.uuid.Uuid

class UserRepositoryImplementation(
    private val database : Database
) : UserRepositoryInterface {

    private fun JdbcTransaction.insert(
        username : String,
        passwordHash : String
    ) : Uuid{
        return UsersTable.insert{
            it[UsersTable.username] = username
            it[UsersTable.passwordHash] = passwordHash
        } get UsersTable.id
    }

    private fun JdbcTransaction.update(
        user : User
    ) {
        UsersTable.update(
            where = { UsersTable.id eq user.id}
        ) {
            it[UsersTable.username] = user.username
            it[UsersTable.passwordHash] = user.passwordHash
        }
    }

    private fun JdbcTransaction.deleteByUUID(
        id: Uuid
    ) : Boolean  {
        return UsersTable.deleteWhere{
            UsersTable.id eq id
        } > 0
    }

    private fun JdbcTransaction.deleteByName(
        username: String
    ) : Boolean {
        return UsersTable.deleteWhere{
            UsersTable.username eq username
        } > 0
    }

    private fun JdbcTransaction.findByUUID(
        id: Uuid
    ) : User? {
        return UsersTable
            .selectAll()
            .where { UsersTable.id eq id }
            .singleOrNull()
            ?.toUser()
    }

    private fun JdbcTransaction.findByName(
        username : String
    ) : User?{
        return UsersTable
            .selectAll()
            .where { UsersTable.username eq username }
            .singleOrNull()
            ?.toUser()
    }

    private fun JdbcTransaction.getAll(

    ) : List<User> {
        return UsersTable.selectAll().map{it.toUser()}
    }

    override suspend fun createUser(
        username : String,
        passwordHash : String
    ) : Uuid {
        return dbQuery {
            insert(
                username,
                passwordHash
            )
        }
    }

    override suspend fun updateAndReturn(
        user : User
    ) : User {
        return dbQuery {
            update(
                user
            )
            findByUUID(user.id) ?: throw ResourceNotFoundException("user updated")
        }
    }

    override suspend fun deleteByName(username: String): Boolean {
        return dbQuery{
            deleteByName(username)
        }
    }

    override suspend fun deleteByUUID(id: Uuid): Boolean {
        return dbQuery{
            deleteByUUID(id)
        }
    }

    override suspend fun findByUUID(id: Uuid): User? {
        return dbQuery{
            findByUUID(id)
        }
    }

    override suspend fun findByName(username: String): User? {
        return dbQuery{
            findByName(username)
        }
    }

    override suspend fun getAll() : List<User> {
        return dbQuery {
            getAll()
        }
    }

    private suspend fun <T> dbQuery(
        block: suspend JdbcTransaction.() -> T
    ): T = suspendTransaction(db = database) {
        block()
    }

    private fun ResultRow.toUser(): User {
        return User(
            id = this[UsersTable.id],
            username = this[UsersTable.username],
            passwordHash = this[UsersTable.passwordHash],
            createdAt = this[UsersTable.createdAt]
        )
    }
}