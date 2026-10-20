package service

import exception.ForbiddenException
import exception.ResourceNotFoundException
import exception.ValidationException
import model.sign.Sign
import model.sign.SignStatus
import org.flywaydb.core.api.resource.Resource
import org.slf4j.LoggerFactory
import repository.SignRepositoryInterface
import service.interfaces.SignServiceInterface
import java.time.OffsetDateTime
import kotlin.collections.emptyList
import kotlin.uuid.Uuid

class SignService(
    private val signRepository : SignRepositoryInterface
) : SignServiceInterface{
    private val logger = LoggerFactory.getLogger(SignService::class.java)
    override suspend fun create(
        userId : Uuid,
        authorId: Uuid,
        text: String,
        status: SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ): Sign {
        logger.info("Starting to check arguments for sign to create")
        if(userId != authorId){
            logger.warn("Requester is not allowed to create signId")
            throw ForbiddenException()
        }
        if(text.isBlank()){
            logger.warn("Sign text to create should not be blank")
            throw ValidationException(mapOf(Pair("createSign","signText is empty")))
        }
        val uuid = signRepository.create(
            authorId = authorId,
            text = text,
            status = status,
            longitude = longitude,
            latitude = latitude,
            expiresAt = expiresAt
        )
        val sign = signRepository.searchById(uuid)
        if(sign == null){
            logger.warn("Sign to create is not found")
            throw ResourceNotFoundException("Created sign")
        }else{
            return sign
        }
    }

    override suspend fun update(
        userId : Uuid,
        id: Uuid,
        text: String,
        status: SignStatus,
        longitude: Double,
        latitude: Double,
        expiresAt: OffsetDateTime
    ): Sign {
        logger.info("Starting to check arguments for sign to update")
        if(text.isBlank()){
            logger.warn("Sign text to update should not be blank")
            throw ValidationException(mapOf(Pair("createSign","signText is empty")))
        }
        val signFound = signRepository.searchById(id)
        if(signFound == null){
            logger.warn("Sign to update does not exist")
            throw ResourceNotFoundException("sign to update")
        }
        if(signFound.authorId != userId){
            logger.warn("Requester is not allowed to update a sign")
            throw ForbiddenException()
        }
        return signRepository.updateAndReturn(
            id,
            text,
            status,
            longitude,
            latitude,
            expiresAt
        )
    }

    override suspend fun getAll() : List<Sign>{
//        logger.info("Starting to get signs")
        val signs = signRepository.getAll()
//        if(signs == emptyList<Sign>()){
//            logger.warn("Signs to get do not exist")
//            throw ResourceNotFoundException("signs to find")
//        }
        return signs
    }

    override suspend fun deleteExpired() : Int{
        return signRepository.deleteExpired()
    }

    override suspend fun deleteById(
        id: Uuid,
        userId : Uuid
    ): Boolean {
        logger.info("Starting to check signs to delete by id")
        val signFound = signRepository.searchById(id)
        if(signFound == null){
            logger.warn("Signs to delete by id do not exist")
            throw ResourceNotFoundException("sign to delete")
        }
        if(signFound.authorId != userId){
            logger.warn("Requester not allowed to delete sign by id")
            throw ForbiddenException()
        }
        val deletionConfirmed = signRepository.deleteById(id)
        return deletionConfirmed
    }

    override suspend fun deleteByAuthorId(
        userId : Uuid,
        authorId: Uuid
    ): Int {
        logger.info("Starting to check signs to delete by author id")
        if(userId != authorId){
            logger.warn("Requester not allowed to delete sign by author id")
            throw ForbiddenException()
        }
        val signsFound = signRepository.searchByAuthorId(authorId)
        if(signsFound == emptyList<Sign>()){
            logger.warn("Signs to delete by author id do not exist")
            throw ResourceNotFoundException("sign to delete")
        }
        val deletionCounter = signRepository.deleteByAuthorId(authorId)
        return deletionCounter
    }

    override suspend fun searchById(id: Uuid): Sign {
        logger.info("Starting to check signs to find by id")
        val sign = signRepository.searchById(id)
        if(sign == null){
            logger.warn("Sign to find by id do not exist")
            throw ResourceNotFoundException("sign to find")
        }else{
            return sign
        }
    }

    override suspend fun searchByAuthorId(authorId: Uuid): List<Sign> {
//        logger.info("Starting to check signs to find by author id")
        val signList = signRepository.searchByAuthorId(authorId)
//        if(signList == emptyList<Sign>()){
//            logger.warn("Sign to find by author id do not exist")
//            throw ResourceNotFoundException("signs to find")
//        }
        return signList
    }

    override suspend fun searchByLocation(
        longitude: Double,
        latitude: Double
    ): List<Sign> {
//        logger.info("Starting to check signs to find by location")
        val signList = signRepository.searchByLocation(
            longitude,
            latitude
        )
//        if(signList == emptyList<Sign>()){
//            logger.warn("Sign to find by location do not exist")
//            throw ResourceNotFoundException("signs to find")
//        }
        return signList
    }
}

