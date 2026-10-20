package repository

import model.sign_report.SignReport
import model.sign_report.SignReportReason
import kotlin.uuid.Uuid

interface SignReportRepositoryInterface {
    suspend fun create(
        userId : Uuid,
        signId : Uuid,
        reason : SignReportReason
    ) : Uuid
    suspend fun updateAndReturn(
        id : Uuid,
        reason : SignReportReason
    ) : SignReport
    suspend fun deleteById(
        id : Uuid
    ) : Boolean
    suspend fun deleteBySignId(
        signId : Uuid
    ) : Int
    suspend fun deleteByUserId(
        userId : Uuid
    ) : Int
    suspend fun deleteBySignAndUserId(
        signId : Uuid,
        userId : Uuid
    ) : Boolean
    suspend fun findById(
        id : Uuid
    ) : SignReport?
    suspend fun findBySignId(
        signId : Uuid
    ) : List<SignReport>
    suspend fun findByUserId(
        userId : Uuid
    ) : List<SignReport>
    suspend fun findBySignAndUserId(
        signId : Uuid,
        userId : Uuid,
    ) : SignReport?
    suspend fun getAll() : List<SignReport>
}