package service

import exception.ForbiddenException
import exception.IdAlreadyExistsException
import exception.ResourceNotFoundException
import model.sign.Sign
import model.sign_report.SignReport
import model.sign_report.SignReportReason
import org.slf4j.LoggerFactory
import repository.SignReportRepositoryInterface
import service.interfaces.SignReportServiceInterface
import kotlin.uuid.Uuid

class SignReportService(
    private val signReportRepository : SignReportRepositoryInterface
) : SignReportServiceInterface {
    private val logger = LoggerFactory.getLogger(SignReportService::class.java)
    override suspend fun create(
        requesterId : Uuid,
        userId: Uuid,
        signId: Uuid,
        reason: SignReportReason
    ): SignReport {
        logger.info("Starting to check if report to create exists")
        val checkIfExists = signReportRepository.findBySignAndUserId(
            userId,
            signId
        )
        if(checkIfExists != null){
            logger.warn("Report with these ids already exists")
            throw IdAlreadyExistsException("Report with these ids already exists")
        }
        if(userId != requesterId){
            logger.warn("Report is not allowed for requester")
            throw ForbiddenException()
        }
        val uuid = signReportRepository.create(
            userId,
            signId,
            reason
        )
        val signReport = signReportRepository.findById(uuid)
        if(signReport == null){
            logger.warn("Report to create is not found")
            throw ResourceNotFoundException("created sign report")
        }else{
            return signReport
        }
    }

    override suspend fun update(
        requesterId : Uuid,
        id: Uuid,
        reason: SignReportReason
    ): SignReport {
        logger.info("Starting to check if report to update exists")
        val signReport = signReportRepository.findById(id)
        if(signReport == null){
            logger.warn("Report to update does not exist")
            throw ResourceNotFoundException("sign report to update")
        }
        if(signReport.userId != requesterId){
            logger.warn("Requester is not allowed to update")
            throw ForbiddenException()
        }
        val returnSignReport = signReportRepository.updateAndReturn(
            id,
            reason
        )
        return returnSignReport
    }

    override suspend fun deleteById(requesterId: Uuid, id: Uuid): Boolean {
        logger.info("Starting to check if report exists by id")
        val signReport = signReportRepository.findById(id)
        if(signReport == null){
            logger.warn("Report to delete by id does not exist")
            throw ResourceNotFoundException("sign report to delete")
        }
        if(signReport.userId != requesterId){
            logger.warn("Requester is not allowed to delete by id")
            throw ForbiddenException()
        }
        val deletionConfirmed = signReportRepository.deleteById(id)
        return deletionConfirmed
    }

    override suspend fun deleteBySignId(requesterId: Uuid, signId: Uuid): Int {
        logger.info("Starting to check if report exists by sign id")
        val signReport = signReportRepository.findBySignId(signId)
        if(signReport == emptyList<SignReport>()){
            logger.warn("Reports to delete by sign id do not exist")
            throw ResourceNotFoundException("sign reports to delete")
        }
        var counter = 0
        for(i in signReport){
            if(i.userId == requesterId){
                counter += if(signReportRepository.deleteById(i.id)) 1 else 0
            }
        }
        return counter
    }

    override suspend fun deleteByUserId(requesterId: Uuid, userId: Uuid): Int {
        logger.info("Starting to check if report exists by user id")
        val signReport = signReportRepository.findByUserId(userId)
        if(signReport == emptyList<SignReport>()){
            logger.warn("Reports to delete by user id do not exist")
            throw ResourceNotFoundException("sign reports to delete")
        }
        if(requesterId != userId){
            logger.warn("Requester is not allowed to delete report by userId")
            throw ForbiddenException()
        }
        val deletionCounter = signReportRepository.deleteByUserId(userId)
        return deletionCounter
    }

    override suspend fun deleteByUserAndSignId(requesterId: Uuid, userId: Uuid, signId: Uuid) : Boolean{
        logger.info("Starting to check if report exists by user and sign id")
        if(signReportRepository.findBySignAndUserId(signId,userId) == null){
            logger.warn("Reports to delete by user and sign id do not exist")
            throw ResourceNotFoundException("sign report to delete")
        }
        if(requesterId != userId){
            logger.warn("Requester is not allowed to delete report by user and sign id")
            throw ForbiddenException()
        }
        val deletionConfirmed = signReportRepository.deleteBySignAndUserId(signId,userId)
        return deletionConfirmed
    }

    override suspend fun findById(id: Uuid): SignReport {
        logger.info("Starting to check if report to find exists")
        val signReport = signReportRepository.findById(id)
        if(signReport == null){
            logger.warn("Report to find by id does not exist")
            throw ResourceNotFoundException("sign report to find")
        } else{
          return signReport
        }
    }

    override suspend fun findByUserId(userId: Uuid): List<SignReport> {
//        logger.info("Starting to check if report to find by user id exists")
        val signReportList = signReportRepository.findByUserId(userId)
//        if(signReportList == emptyList<SignReport>()){
//            logger.warn("Report to find by user id does not exist")
//            throw ResourceNotFoundException("sign reports to find")
//        }
        return signReportList
    }

    override suspend fun findBySignId(signId: Uuid): List<SignReport> {
//        logger.info("Starting to check if report to find by sign id exists")
        val signReportList = signReportRepository.findBySignId(signId)
//        if(signReportList == emptyList<SignReport>()){
//            logger.warn("Report to find by sign id does not exist")
//            throw ResourceNotFoundException("sign reports to find")
//        }
        return signReportList
    }

    override suspend fun findBySignAndUserId(userId: Uuid, signId: Uuid) : SignReport{
        logger.info("Starting to check if report to find by user and sign id exists")
        val signReport = signReportRepository.findBySignAndUserId(signId, userId)
        if(signReport == null){
            logger.warn("Report to find by sign and user id does not exist")
            throw ResourceNotFoundException("sign report to find")
        }else{
            return signReport
        }
    }
    override suspend fun getAll() : List<SignReport> {
//        logger.info("Starting to check if reports exist")
        val signReports = signReportRepository.getAll()
//        if(signReports == emptyList<SignReport>()){
//            logger.warn("Reports to get do not exist")
//            throw ResourceNotFoundException("sign reports to find")
//        }
        return signReports
    }

}