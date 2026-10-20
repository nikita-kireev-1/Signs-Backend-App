package service

import exception.ForbiddenException
import exception.IdAlreadyExistsException
import exception.ResourceNotFoundException
import model.sign_reaction.SignReaction
import model.sign_reaction.SignReactionType
import org.slf4j.LoggerFactory
import repository.SignReactionRepositoryInterface
import service.interfaces.SignReactionServiceInterface
import kotlin.uuid.Uuid

class SignReactionService (
    private val signReactionRepository : SignReactionRepositoryInterface
) : SignReactionServiceInterface {
    private val logger = LoggerFactory.getLogger(SignReactionService::class.java)
    override suspend fun create(
        userId: Uuid,
        signId: Uuid,
        type: SignReactionType
    ): SignReaction {
        logger.info("Starting to check arguments for create")
        if(signReactionRepository.findByUserAndSignId(userId, signId) != null){
            logger.warn("Reaction already exists with such $userId,$signId",userId,signId)
            throw IdAlreadyExistsException("Reaction with these ids already exists")
        }
        val uuid = signReactionRepository.create(
            userId,
            signId,
            type
        )
        val signReaction = signReactionRepository.findById(uuid)
        if(signReaction == null){
            logger.warn("Reaction not found for returning")
            throw ResourceNotFoundException("Created sign reaction")
        }else{
            return signReaction
        }
    }

    override suspend fun update(
        id: Uuid,
        userId : Uuid,
        type: SignReactionType
    ): SignReaction {
        logger.info("Starting to check arguments for update")
        val signReaction = signReactionRepository.findById(id)
        if(signReaction == null){
            logger.warn("Reaction to update is not found")
            throw ResourceNotFoundException("sign reaction to update")
        }
        if(signReaction.userId != userId){
            logger.warn("Requester is not allowed")
            throw ForbiddenException()
        }
        val returnSign = signReactionRepository.updateAndReturn(
            id,
            type
        )
        return returnSign
    }

    override suspend fun getAll() : List<SignReaction>{
//        logger.info("Starting to check if reactions exist")
        val signReactions = signReactionRepository.getAll()
//        if(signReactions == emptyList<SignReaction>()){
//            logger.warn("Reactions not found")
//            throw ResourceNotFoundException("sign reactions to find")
//        }
        return signReactions
    }

    override suspend fun deleteById(id: Uuid,userId : Uuid): Boolean {
        logger.info("Starting to check if reaction exist by id")
        val signReaction = signReactionRepository.findById(id)
        if(signReaction == null){
            logger.warn("Reaction asked to delete doesn't exist")
            throw ResourceNotFoundException("sign reaction to delete")
        }
        if(signReaction.userId != userId){
            logger.warn("Requester not allowed to delete by id")
            throw ForbiddenException()
        }
        return signReactionRepository.deleteById(id)
    }

    override suspend fun deleteBySignId(userId : Uuid, signId: Uuid): Int {
        logger.info("Starting to check if reaction exist by signId")
        val signReaction = signReactionRepository.findBySignId(signId)
        if(signReaction == emptyList<SignReaction>()){
            logger.warn("Reactions asked to delete do not exist")
            throw ResourceNotFoundException("sign reactions to delete")
        }
        var counter = 0
        for(i in signReaction){
            if(i.userId == userId){
                signReactionRepository.deleteById(i.id)
                counter++
            }
        }
        return counter
    }

    override suspend fun deleteByUserId(requesterId : Uuid, userId: Uuid): Int {
        logger.info("Starting to check if reaction exist by userId")
        val signReaction = signReactionRepository.findByUserId(userId)
        if(signReaction == emptyList<SignReaction>()){
            logger.warn("Reaction asked to delete does not exist")
            throw ResourceNotFoundException("sign reactions to delete")
        }
        if(requesterId != userId){
            logger.warn("Requester not allowed to delete by userId")
            throw ForbiddenException()
        }
        return signReactionRepository.deleteByUserId(userId)
    }

    override suspend fun deleteByUserAndSignId(requesterId : Uuid, userId: Uuid, signId: Uuid): Boolean {
        logger.info("Starting to check if reaction exist by user and sign ids")
        val signReaction = signReactionRepository.findByUserAndSignId(userId, signId)
        if(signReaction == null){
            logger.warn("Reaction asked to delete does not exist by sign and user id")
            throw ResourceNotFoundException("sign reaction to delete")
        }
        if(signReaction.userId != requesterId){
            logger.warn("Requester not allowed to delete")
            throw ForbiddenException()
        }
        return signReactionRepository.deleteByUserAndSignId(userId,signId)
    }

    override suspend fun findById(id: Uuid): SignReaction {
        logger.info("Starting to check if reaction exist to find by id")
        val signReaction = signReactionRepository.findById(id)
        if(signReaction == null){
            logger.warn("Sign reaction does not exist (id)")
            throw ResourceNotFoundException("sign reaction to find")
        }else{
            return signReaction
        }
    }

    override suspend fun findByUserId(userId: Uuid): List<SignReaction> {
//        logger.info("Starting to check if reaction exist to find by userid")
        val signReactionList = signReactionRepository.findByUserId(userId)
//        if(signReactionList == emptyList<SignReaction>()){
//            logger.warn("Sign reactions do not exist (userId)")
//            throw ResourceNotFoundException("sign reactions to find")
//        }
        return signReactionList
    }

    override suspend fun findBySignId(signId: Uuid): List<SignReaction> {
//        logger.info("Starting to check if reaction exist to find by signId")
        val signReactionList = signReactionRepository.findBySignId(signId)
//        if(signReactionList == emptyList<SignReaction>()){
//            logger.warn("Sign reactions do not exist(signId)")
//            throw ResourceNotFoundException("sign reactions to find")
//        }
        return signReactionList
    }

    override suspend fun findBySignAndUserId(
        userId: Uuid,
        signId: Uuid
    ): SignReaction {
        logger.info("Starting to check if reaction exist to find by sign and user id")
        val signReaction = signReactionRepository.findByUserAndSignId(userId,signId)
        if(signReaction == null){
            logger.warn("Sign reaction do not exist(sign and user id)")
            throw ResourceNotFoundException("sign reaction to find")
        }else{
            return signReaction
        }
    }

}