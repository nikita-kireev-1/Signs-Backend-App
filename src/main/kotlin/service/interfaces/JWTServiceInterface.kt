package service.interfaces

import com.auth0.jwt.JWTVerifier

interface JWTServiceInterface {
    fun generateToken( userId : String ) : String
    fun verifier() : JWTVerifier
}