package com.andef.route.auth.client

import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import com.andef.dto.auth.client.ClientLoginRequest
import com.andef.dto.auth.client.ClientPasswordChangeRequest
import com.andef.dto.auth.client.ClientRegisterRequest
import com.andef.service.auth.client.ClientAuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Application.configureClientAuthRoutes(connection: Connection) {
    val service = ClientAuthService(connection)
    routing {
        route("/auth-client") {
            configureClientLogin(service)
            configureClientRegister(service)
            configureClientPasswordChange(service)
            configureClientCheckToken()
        }
    }
}

private fun Route.configureClientLogin(service: ClientAuthService) {
    post("/login") {
        val clientLoginRequest = call.receive<ClientLoginRequest>()
        service.login(clientLoginRequest)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}

private fun Route.configureClientRegister(service: ClientAuthService) {
    post("/register") {
        val clientRegisterRequest = call.receive<ClientRegisterRequest>()
        service.register(clientRegisterRequest)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}

private fun Route.configureClientPasswordChange(service: ClientAuthService) {
    post("/password-change") {
        val clientPasswordChangeRequest = call.receive<ClientPasswordChangeRequest>()
        service.passwordChange(clientPasswordChangeRequest)?.let {
            call.respond(it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }
}

private fun Route.configureClientCheckToken() {
    authenticate(CLIENT_AUTH_JWT) {
        get("/check-token") {
            call.respond(HttpStatusCode.OK)
        }
    }
}