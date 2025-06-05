package com.andef.config

import at.favre.lib.crypto.bcrypt.BCrypt
import com.andef.config.SecurityConfig.CLIENT_AUDIENCE
import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import io.ktor.server.application.*
import io.ktor.server.auth.*

object SecurityConfig {
    const val CLIENT_AUTH_JWT = "client-auth-jwt"
    const val CLIENT_AUDIENCE = "client-audience"
    const val ID_CLAIM = "id-claim"

    fun passwordHash(password: String): String = BCrypt
        .withDefaults()
        .hashToString(12, password.toCharArray())

    fun passwordVerify(password: String, bcryptHash: String): Boolean = BCrypt
        .verifyer()
        .verify(password.toCharArray(), bcryptHash.toCharArray())
        .verified
}

fun Application.configureSecurity() {
    install(Authentication) {
        jwtConfig(CLIENT_AUTH_JWT, CLIENT_AUDIENCE)
    }
}