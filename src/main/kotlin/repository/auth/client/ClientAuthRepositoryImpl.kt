package com.andef.repository.auth.client

import com.andef.config.ALGORITHM
import com.andef.config.ISSUER
import com.andef.config.SecurityConfig.CLIENT_AUDIENCE
import com.andef.config.SecurityConfig.ID_CLAIM
import com.andef.config.SecurityConfig.passwordHash
import com.andef.config.SecurityConfig.passwordVerify
import com.andef.dto.auth.client.ClientAuthResponse
import com.andef.model.Client
import com.auth0.jwt.JWT
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.sql.Connection
import java.sql.Date
import java.sql.ResultSet
import java.sql.Statement.RETURN_GENERATED_KEYS

class ClientAuthRepositoryImpl(private val connection: Connection) : ClientAuthRepository {
    override suspend fun login(phone: String, password: String): ClientAuthResponse? {
        val resultSet = selectClientByPhone(phone)
        return if (resultSet.next()) {
            val clientId = resultSet.getInt(Client.ID)
            val bcryptHash = resultSet.getString(Client.PASSWORD)
            when (passwordVerify(password, bcryptHash)) {
                true -> ClientAuthResponse(generateClientToken(clientId))
                false -> null
            }
        } else null
    }

    override suspend fun register(
        surname: String,
        name: String,
        phone: String,
        email: String,
        password: String,
        birthday: LocalDate,
        gender: String
    ): ClientAuthResponse? {
        val resultSet = selectClientByPhoneOrEmail(phone, email)
        return if (!resultSet.next()) {
            val hashedPassword = passwordHash(password)
            val params = mapOf(1 to surname, 2 to name, 3 to phone, 4 to email, 5 to hashedPassword, 7 to gender)
            val statement = connection.prepareStatement(INSERT_CLIENT, RETURN_GENERATED_KEYS)
            val rowsInserted = statement.apply {
                params.forEach { i, param -> setString(i, param) }
                setDate(6, Date.valueOf(birthday.toJavaLocalDate()))
            }.executeUpdate()
            when (rowsInserted > 0) {
                true -> {
                    val generatedKeys = statement.generatedKeys
                    if (generatedKeys.next()) {
                        val clientId = statement.generatedKeys.getInt(1)
                        ClientAuthResponse(generateClientToken(clientId))
                    } else null
                }

                false -> null
            }
        } else null
    }

    override suspend fun passwordChange(phone: String, password: String): ClientAuthResponse? {
        val resultSet = selectClientByPhone(phone)
        return if (resultSet.next()) {
            val clientId = resultSet.getInt(Client.ID)
            val hashedPassword = passwordHash(password)
            val rowsUpdated = connection.prepareStatement(UPDATE_CLIENT_PASSWORD_BY_ID).apply {
                setString(1, hashedPassword)
                setInt(2, clientId)
            }.executeUpdate()
            when (rowsUpdated > 0) {
                true -> ClientAuthResponse(generateClientToken(clientId))
                false -> null
            }
        } else null
    }

    private fun selectClientByPhone(phone: String): ResultSet {
        return connection.prepareStatement(SELECT_CLIENT_BY_PHONE).apply {
            setString(1, phone)
        }.executeQuery()
    }

    private fun selectClientByPhoneOrEmail(phone: String, email: String): ResultSet {
        return connection.prepareStatement(SELECT_CLIENT_BY_PHONE_OR_EMAIL).apply {
            setString(1, phone)
            setString(2, email)
        }.executeQuery()
    }

    companion object {
        private const val SELECT_CLIENT_BY_PHONE = "SELECT * FROM ${Client.TABLE_NAME} WHERE ${Client.PHONE} = ?"

        private const val SELECT_CLIENT_BY_PHONE_OR_EMAIL = "SELECT * FROM ${Client.TABLE_NAME} " +
                "WHERE ${Client.PHONE} = ? OR ${Client.EMAIL} = ?"

        private const val UPDATE_CLIENT_PASSWORD_BY_ID = "UPDATE ${Client.TABLE_NAME} SET ${Client.PASSWORD} = ?" +
                " WHERE ${Client.ID} = ?"

        private const val INSERT_CLIENT = "INSERT INTO ${Client.TABLE_NAME} (${Client.SURNAME}, ${Client.NAME}," +
                " ${Client.PHONE}, ${Client.EMAIL}, ${Client.PASSWORD}, ${Client.BIRTHDAY}, ${Client.GENDER})" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)"

        private fun generateClientToken(clientId: Int) = JWT
            .create()
            .withAudience(CLIENT_AUDIENCE)
            .withIssuer(ISSUER)
            .withClaim(ID_CLAIM, clientId)
            .sign(ALGORITHM)
    }
}