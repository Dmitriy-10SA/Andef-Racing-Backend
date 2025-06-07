package com.andef.route.booking.client

import com.andef.config.SecurityConfig.CLIENT_AUTH_JWT
import com.andef.config.SecurityConfig.ID_CLAIM
import com.andef.dto.booking.client.BookingRequestDto
import com.andef.route.requestWithParameter
import com.andef.service.booking.client.ClientBookingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDate
import java.sql.Connection

fun Application.configureClientBookingRoutes(connection: Connection) {
    val service = ClientBookingService(connection)
    routing {
        route("/booking-client") {
            configureGetAllBookingByClientId(service)
            configureGetClubByClubId(service)
            configureClientBooking(service)
            configureClientGetFreeTimesForDate(service)
            configureClientBookingPay(service)
        }
    }
}

private fun Route.configureGetAllBookingByClientId(service: ClientBookingService) {
    authenticate(CLIENT_AUTH_JWT) {
        get("/all-booking") {
            val clientId = call.principal<JWTPrincipal>()!!.payload.getClaim(ID_CLAIM).asInt()
            call.respond(service.getAllBookingByClientId(clientId))
        }
    }
}

private fun Route.configureGetClubByClubId(service: ClientBookingService) {
    authenticate(CLIENT_AUTH_JWT) {
        get("/club/{clubId}") {
            requestWithParameter(
                parameter = "clubId",
                transformFun = { it.toInt() },
                action = {
                    call.respond(service.getClubByClubId(it) ?: HttpStatusCode.NoContent)
                }
            )
        }
    }
}

private fun Route.configureClientBooking(service: ClientBookingService) {
    authenticate(CLIENT_AUTH_JWT) {
        put("/booking") {
            val clientId = call.principal<JWTPrincipal>()!!.payload.getClaim(ID_CLAIM).asInt()
            val bookingRequestDto = call.receive<BookingRequestDto>()
            service.booking(clientId, bookingRequestDto)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.FailedDependency)
        }
    }
}

private fun Route.configureClientGetFreeTimesForDate(service: ClientBookingService) {
    authenticate(CLIENT_AUTH_JWT) {
        get("/free-times/{date}") {
            requestWithParameter(
                parameter = "date",
                transformFun = { LocalDate.parse(it) },
                action = {
                    call.respond(service.getFreeTimesForDate(it))
                }
            )
        }
    }
}

private fun Route.configureClientBookingPay(service: ClientBookingService) {
    authenticate(CLIENT_AUTH_JWT) {
        put("/booking-pay/{bookingId}") {
            requestWithParameter(
                parameter = "bookingId",
                transformFun = { it.toInt() },
                action = {
                    service.bookingPay(it)?.let {
                        call.respond(HttpStatusCode.OK)
                    } ?: call.respond(HttpStatusCode.FailedDependency)
                }
            )
        }
    }
}