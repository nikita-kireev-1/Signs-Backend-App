package config

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.ApplicationStopping
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import repository.SignRepositoryInterface
import service.UserService
import service.interfaces.SignReportServiceInterface
import service.interfaces.SignServiceInterface
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun Application.configureCleanUp(
    signService : SignServiceInterface,
    signRepositoryImplementation : SignRepositoryInterface
){
    val logger = LoggerFactory.getLogger(Application::class.java)
    val backgroundScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.IO +
                CoroutineExceptionHandler { _, exception ->
                    logger.warn("Coroutine failed with $exception")
                }
    )

    environment.monitor.subscribe(ApplicationStarted) {
        backgroundScope.launch(CoroutineName("DeleteExpired")) {
            while (isActive) {
                try {
                    logger.info("Deleting expired signs")
                    val counter = signService.deleteExpired()
                    logger.info("Deleted $counter expired signs")
                }catch(e : Exception){
                    logger.warn("exception ${e.message} for deleting expired")
                }
                delay(1.hours)
            }
        }
        backgroundScope.launch(CoroutineName("DeleteReported")) {
            while(isActive) {
                try {
                    logger.info("Deleting highly reported signs")
                    val deletedCounter = signRepositoryImplementation.deleteHighlyReportedSigns(5)
                    logger.info("Deleted $deletedCounter reported signs")
                } catch(e : Exception){
                    logger.warn("exception ${e.message} for deleting reported")
                }
                delay(1.hours)
            }
        }
    }

    environment.monitor.subscribe(ApplicationStopping) {
        logger.info("Server stopping, coroutine cancelling")
        backgroundScope.cancel()
    }
}