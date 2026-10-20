package service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import config.JwtConfig
import org.slf4j.LoggerFactory
import service.interfaces.JWTServiceInterface
import java.util.Date

class JWTService(
    private val config: JwtConfig
) : JWTServiceInterface {

    private val logger = LoggerFactory.getLogger(JWTService::class.java)

    private val algorithm = Algorithm.HMAC256(config.secret)

    private val verifier: JWTVerifier =
        JWT
            .require(algorithm)
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .build()

    override fun generateToken(userId: String): String {
        logger.info("Generating tokens")
        val expiresAt = Date(
            System.currentTimeMillis() + config.expiresInMs
        )

        return JWT
            .create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withClaim("userId", userId)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
    }

    override fun verifier(): JWTVerifier {
        logger.info("Verifying tokens")
        return verifier
    }
}