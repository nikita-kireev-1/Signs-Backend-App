import config.configureCleanUp
import config.configureCompression
import config.configureLogging
import config.configureRouting
import config.configureSecurity
import config.configureSerialization
import config.configureStatusPages
import config.installKoin
import config.toAppConfig
import di.appModule
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import repository.Implementation.SignReportRepositoryImplementation
import repository.SignReportRepositoryInterface
import repository.SignRepositoryInterface
import config.configureSpecification
import service.interfaces.JWTServiceInterface
import service.interfaces.SignServiceInterface
import java.io.File
import java.util.Properties

fun main() {
    val localPropsFile = File("local.properties")
    if (localPropsFile.exists()) {
        val props = Properties()
        localPropsFile.inputStream().use { props.load(it) }
        props.forEach { key, value ->
            System.setProperty(key.toString(), value.toString())
        }
        println("success loading local.properties")
    } else {
        println("local.properties not found")
    }
    val port = System.getenv("KTOR_DEPLOYMENT_PORT")?.toIntOrNull()
        ?: System.getenv("PORT")?.toIntOrNull()
        ?: System.getProperty("ktor.deployment.port")?.toIntOrNull()
        ?: 8080

    val host = System.getenv("KTOR_DEPLOYMENT_HOST")
        ?: System.getProperty("ktor.deployment.host")
        ?: "0.0.0.0"
    embeddedServer(
        factory = Netty,
        port = port,
        host = host,
        module = Application::module
    ).start(wait = true)
}

fun Application.module(){
    val appConfig = environment.config.toAppConfig()
    installKoin(appConfig)
    val database = get<Database>()
    configureSpecification()
    configureSerialization()
    configureStatusPages()
    configureLogging()
    val jwtService by inject<JWTServiceInterface>()
    configureSecurity(jwtService)
    configureRouting()
    configureCompression()
    val signService by inject<SignServiceInterface>()
    val signRepository by inject<SignRepositoryInterface>()
    configureCleanUp(signService,signRepository)

}