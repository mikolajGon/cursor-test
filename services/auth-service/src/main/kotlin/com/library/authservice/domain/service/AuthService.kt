package com.library.authservice.domain.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.library.authservice.domain.repository.AuthRepository

class AuthService(private val authRepository: AuthRepository, private val jwtService: JwtService) {
    suspend fun authenticate(username: String, password: String): String? {
        val user = authRepository.findByUsername(username)
        return if (user != null &&
                        BCrypt.verifyer()
                                .verify(password.toCharArray(), user.password.toCharArray())
                                .verified
        ) {
            jwtService.generateToken(user.username)
        } else null
    }
}
