package com.andef

import com.andef.config.connectToPostgres
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    connectToPostgres(onConnected = { connection ->

    })
}
