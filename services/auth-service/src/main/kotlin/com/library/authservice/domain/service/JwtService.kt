package com.library.authservice.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*
import org.slf4j.LoggerFactory

class JwtService {
    private val logger = LoggerFactory.getLogger(JwtService::class.java)
    private val issuer = "library-auth"
    private val audience = "library-users"
    private val algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET") ?: "your-secret-key")

    fun generateToken(username: String): String =
            JWT.create()
                    .withSubject(username)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withClaim("username", username)
                    .withClaim("roles", listOf("USER"))
                    .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .sign(algorithm)

    fun verifyToken(token: String) = JWT.require(algorithm).build().verify(token)
}
