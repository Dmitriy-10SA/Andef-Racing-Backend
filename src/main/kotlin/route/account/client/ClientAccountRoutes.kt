package com.andef.route.account.client

import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import com.andef.config.SecurityConfig.ID_CLAIM
import com.andef.route.requestWithParameter
import com.andef.service.account.client.ClientAccountService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Application.configureClientAccountRoutes(connection: Connection) {
    val service = ClientAccountService(connection)
    routing {
        route("/account-client") {
            configureClientGetInfo(service)
            configureClientChangeEmail(service)
            configureClientChangePhone(service)
        }
    }
}

private fun Route.configureClientGetInfo(service: ClientAccountService) {
    authenticate(CLIENT_AUTH_JWT) {
        get("/get-info") {
            val clientId = call.principal<JWTPrincipal>()!!.payload.getClaim(ID_CLAIM).asInt()
            service.getInfo(clientId)?.let {
                call.respond(it)
            } ?: call.respond(HttpStatusCode.Unauthorized)
        }
    }
}

private fun Route.configureClientChangeEmail(service: ClientAccountService) {
    authenticate(CLIENT_AUTH_JWT) {
        put("/change-email/{email}") {
            val clientId = call.principal<JWTPrincipal>()!!.payload.getClaim(ID_CLAIM).asInt()
            requestWithParameter(
                parameter = "email",
                transformFun = { it },
                action = {
                    service.changeEmail(clientId, it)?.let {
                        call.respond(HttpStatusCode.OK)
                    } ?: call.respond(HttpStatusCode.Unauthorized)
                }
            )
        }
    }
}

private fun Route.configureClientChangePhone(service: ClientAccountService) {
    authenticate(CLIENT_AUTH_JWT) {
        put("/change-phone/{phone}") {
            val clientId = call.principal<JWTPrincipal>()!!.payload.getClaim(ID_CLAIM).asInt()
            requestWithParameter(
                parameter = "phone",
                transformFun = { it },
                action = {
                    service.changePhone(clientId, it)?.let {
                        call.respond(HttpStatusCode.OK)
                    } ?: call.respond(HttpStatusCode.Unauthorized)
                }
            )
        }
    }
}