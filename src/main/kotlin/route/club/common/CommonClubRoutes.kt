package com.andef.route.club.common

import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import com.andef.service.club.common.CommonClubService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Application.configureCommonClubRoutes(connection: Connection) {
    val service = CommonClubService(connection)
    routing {
        route("/club-common") {
            configureCommonGetAllClubs(service, CLIENT_AUTH_JWT)
            configureCommonGetAllCostsInClub(service, CLIENT_AUTH_JWT)
            configureCommonGetAllEmployeesInClub(service, CLIENT_AUTH_JWT)
        }
    }
}

private fun Route.configureCommonGetAllClubs(service: CommonClubService, authJwt: String) {
    authenticate(authJwt) {
        get("/all-clubs") {
            call.respond(service.getAllClubs())
        }
    }
}

private fun Route.configureCommonGetAllCostsInClub(service: CommonClubService, authJwt: String) {
    authenticate(authJwt) {
        get("/all-costs-in-club/{clubId}") {
            requestWithClubId { clubId ->
                call.respond(service.getAllCostsInClub(clubId))
            }
        }
    }
}

private fun Route.configureCommonGetAllEmployeesInClub(service: CommonClubService, authJwt: String) {
    authenticate(authJwt) {
        get("/all-employees-in-club/{clubId}") {
            requestWithClubId { clubId ->
                call.respond(service.getAllEmployeesInClub(clubId))
            }
        }
    }
}

private suspend fun RoutingContext.requestWithClubId(action: suspend (Int) -> Unit) {
    call.parameters["clubId"]?.let {
        try {
            action(it.toInt())
        } catch (_: Exception) {
            call.respond(HttpStatusCode.BadRequest)
        }
    } ?: call.respond(HttpStatusCode.BadRequest)
}