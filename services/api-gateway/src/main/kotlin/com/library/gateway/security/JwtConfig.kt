package com.library.gateway.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import java.util.*

fun Application.configureJwt() {

    val logger = log
    val secret = "your-secret-key"
    val issuer = "library-auth"
    val audience = "library-users"
    val realm = System.getenv("JWT_REALM") ?: "library"

    logger.info("Configuring JWT with issuer: $issuer, audience: $audience")

    install(Authentication) {
        logger.info("Installing JWT authentication")
        jwt("auth-jwt") {
            this.realm = realm
            authSchemes("Bearer")

            verifier(
                    JWT.require(Algorithm.HMAC256(secret))
                            .withAudience(audience)
                            .withIssuer(issuer)
                            .build()
            )

            validate { credential ->
                try {
                    return@validate when {
                        credential.payload.audience.contains(audience) -> {
                            when (credential.payload.issuer) {
                                issuer -> {
                                    JWTPrincipal(credential.payload)
                                }
                                else -> {
                                    null
                                }
                            }
                        }
                        else -> {
                            null
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Token validation failed", e)
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                        HttpStatusCode.Unauthorized,
                        mapOf("error" to "Invalid or expired token. Please login again.")
                )
            }
        }
    }
}
