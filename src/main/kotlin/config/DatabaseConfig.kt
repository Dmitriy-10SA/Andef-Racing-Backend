package com.andef.config

import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.connectToPostgres(onConnected: (Connection) -> Unit, onError: (Exception) -> Unit = { throw it }) {
    try {
        Class.forName("org.postgresql.Driver")
        val config = environment.config.config("database")
        val url = config.property("url").getString()
        val user = config.property("user").getString()
        val password = config.property("password").getString()
        val connection = DriverManager.getConnection(url, user, password)
        onConnected(connection)
    } catch (e: Exception) {
        onError(e)
    }
}