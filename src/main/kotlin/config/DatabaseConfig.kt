package com.andef.config

import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

private const val CLASS_NAME = "org.postgresql.Driver"
private const val CONFIG_PATH = "database"
private const val URL = "url"
private const val USER = "user"
private const val PASSWORD = "password"

fun Application.connectToPostgres(onConnected: (Connection) -> Unit, onError: (Exception) -> Unit = { throw it }) {
    try {
        Class.forName(CLASS_NAME)
        val config = environment.config.config(CONFIG_PATH)
        val url = config.property(URL).getString()
        val user = config.property(USER).getString()
        val password = config.property(PASSWORD).getString()
        val connection = DriverManager.getConnection(url, user, password)
        onConnected(connection)
    } catch (e: Exception) {
        onError(e)
    }
}