package com.andef.route.club.common

import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import com.andef.route.requestWithParameter
import com.andef.service.club.common.CommonClubService
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
            configureCommonGetAllGames(service, CLIENT_AUTH_JWT)
        }
    }
}

private fun Route.configureCommonGetAllGames(service: CommonClubService, authJwt: String) {
    authenticate(authJwt) {
        get("/all-games") {
            call.respond(service.getAllGames())
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
            requestWithParameter(
                parameter = "clubId",
                transformFun = { it.toInt() },
                action = { call.respond(service.getAllCostsInClub(it)) }
            )
        }
    }
}

private fun Route.configureCommonGetAllEmployeesInClub(service: CommonClubService, authJwt: String) {
    authenticate(authJwt) {
        get("/all-employees-in-club/{clubId}") {
            requestWithParameter(
                parameter = "clubId",
                transformFun = { it.toInt() },
                action = { call.respond(service.getAllEmployeesInClub(it)) }
            )
        }
    }
}