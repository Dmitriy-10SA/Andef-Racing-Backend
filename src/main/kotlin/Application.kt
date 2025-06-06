package com.andef

import com.andef.config.configureSecurity
import com.andef.config.configureSerialization
import com.andef.config.connectToPostgres
import com.andef.route.account.client.configureClientAccountRoutes
import com.andef.route.auth.client.configureClientAuthRoutes
import com.andef.route.club.common.configureCommonClubRoutes
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    connectToPostgres(
        onConnected = { connection ->
            configureSecurity()
            configureSerialization()
            configureClientAuthRoutes(connection)
            configureClientAccountRoutes(connection)
            configureCommonClubRoutes(connection)
        }
    )
}
