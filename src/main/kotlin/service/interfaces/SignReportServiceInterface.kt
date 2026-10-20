package service.interfaces

import model.sign_report.SignReport
import model.sign_report.SignReportReason
import kotlin.uuid.Uuid

interface SignReportServiceInterface {
    suspend fun create(
        requesterId : Uuid,
        userId : Uuid,
        signId : Uuid,
        reason : SignReportReason
    ) : SignReport
    suspend fun update(
        requesterId : Uuid,
        id : Uuid,
        reason : SignReportReason
    ) : SignReport
    suspend fun deleteById(
        requesterId : Uuid,
        id : Uuid
    ) : Boolean
    suspend fun deleteBySignId(
        requesterId : Uuid,
        signId : Uuid
    ) : Int
    suspend fun deleteByUserId(
        requesterId : Uuid,
        userId : Uuid
    ) : Int
    suspend fun deleteByUserAndSignId(
        requesterId : Uuid,
        userId : Uuid,
        signId : Uuid
    ) : Boolean
    suspend fun findById(
        id : Uuid
    ) : SignReport
    suspend fun findByUserId(
        userId : Uuid
    ) : List<SignReport>
    suspend fun findBySignId(
        signId : Uuid
    ) : List<SignReport>
    suspend fun findBySignAndUserId(
        userId : Uuid,
        signId : Uuid
    ) : SignReport
    suspend fun getAll() : List<SignReport>
}