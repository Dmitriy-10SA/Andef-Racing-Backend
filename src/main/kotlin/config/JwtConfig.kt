package com.andef.config

import com.andef.config.SecurityConfig.ID_CLAIM
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

private const val JWT_SECRET = "J241W315T-S24ECRE34T"
private val ALGORITHM = Algorithm.HMAC256(JWT_SECRET)

private const val ISSUER = "ktor-app-andefracingbackend"

private const val ERROR_MESSAGE = "Invalid token"

fun AuthenticationConfig.jwtConfig(authJwt: String, audience: String) {
    jwt(authJwt) {
        verifier(
            JWT
                .require(ALGORITHM)
                .withAudience(audience)
                .withIssuer(ISSUER)
                .build()
        )
        validate { jwtCredential ->
            when (jwtCredential.payload.getClaim(ID_CLAIM) != null) {
                true -> JWTPrincipal(jwtCredential.payload)
                false -> null
            }
        }
        challenge { _, _ -> call.respond(HttpStatusCode.Unauthorized, ERROR_MESSAGE) }
    }
}