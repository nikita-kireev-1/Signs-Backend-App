package config

import io.ktor.server.config.ApplicationConfig

data class AppConfig(
    val server: ServerConfig,
    val database: DatabaseConfig,
    val app: ApplicationSpecificConfig,
    val jwt: JwtConfig
)

data class ServerConfig(
    val port: Int,
    val host: String
)

data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String,
    val driver: String,
    val poolSize: Int = 10
)

data class ApplicationSpecificConfig(
    val environment: Environment = Environment.DEV
)

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val expiresInMs: Long
)

enum class Environment {
    DEV, PROD
}

fun ApplicationConfig.toAppConfig(): AppConfig {
    fun getStringWithFallback(key: String, default: String): String {
        return propertyOrNull(key)?.getString()
            ?: default
    }

    fun getIntWithFallback(key: String, default: Int): Int {
        return propertyOrNull(key)?.getString()?.toIntOrNull()
            ?: default
    }

    fun getLongWithFallback(key : String, default : Long) : Long {
        return propertyOrNull(key)?.getString()?.toLongOrNull()
            ?: default
    }
    val dbUrl = getStringWithFallback(
        key = "db.url",
        default = "jdbc:postgresql://localhost:5432/mydb"
    )

    require(dbUrl.isNotBlank()) { "Database URL cannot be blank. Check application.conf or DATABASE_URL env var." }

    return AppConfig(
        server = ServerConfig(
            port = getIntWithFallback("ktor.deployment.port", 8080),
            host = getStringWithFallback("ktor.deployment.host", "0.0.0.0")
        ),
        database = DatabaseConfig(
            url = dbUrl,
            user = getStringWithFallback("db.user", "postgres"),
            password = getStringWithFallback("db.password", "postgres"),
            driver = getStringWithFallback("db.driver", "org.postgresql.Driver"),
            poolSize = getIntWithFallback("db.pool.size", 10)
        ),
        app = ApplicationSpecificConfig(
            environment = if (getStringWithFallback("app.env", "dev") == "prod") {
                Environment.PROD
            } else {
                Environment.DEV
            }
        ),
        jwt = JwtConfig(
            secret = getStringWithFallback("jwt.secret","change"),
            issuer = getStringWithFallback("jwt.issuer","signs-server"),
            audience = getStringWithFallback("jwt.audience","signs-client"),
            expiresInMs = getLongWithFallback("jwt.expiresInMs",3600000L)
        )
    )
}