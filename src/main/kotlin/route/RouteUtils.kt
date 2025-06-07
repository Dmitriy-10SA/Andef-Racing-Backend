package com.andef.route

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun <T> RoutingContext.requestWithParameter(
    parameter: String,
    transformFun: (String) -> T,
    action: suspend (T) -> Unit
) {
    call.parameters[parameter]?.let {
        try {
            action(transformFun(it))
        } catch (_: Exception) {
            call.respond(HttpStatusCode.BadRequest)
        }
    } ?: call.respond(HttpStatusCode.BadRequest)
}