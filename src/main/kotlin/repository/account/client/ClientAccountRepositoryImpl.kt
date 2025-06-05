package com.andef.repository.account.client

import com.andef.dto.account.client.ClientInfoDto
import com.andef.model.Client
import kotlinx.datetime.toKotlinLocalDate
import java.sql.Connection

class ClientAccountRepositoryImpl(private val connection: Connection) : ClientAccountRepository {
    override suspend fun getInfo(clientId: Int): ClientInfoDto? {
        val resultSet = connection.prepareStatement(SELECT_CLIENT_BY_ID).apply {
            setInt(1, clientId)
        }.executeQuery()
        return if (resultSet.next()) {
            ClientInfoDto(
                surname = resultSet.getString(Client.SURNAME),
                name = resultSet.getString(Client.NAME),
                phone = resultSet.getString(Client.PHONE),
                email = resultSet.getString(Client.EMAIL),
                birthday = resultSet.getDate(Client.BIRTHDAY).toLocalDate().toKotlinLocalDate(),
                gender = resultSet.getString(Client.GENDER)
            )
        } else null
    }

    override suspend fun changePhone(clientId: Int, phone: String): Unit? {
        val rowsUpdated = connection.prepareStatement(UPDATE_CLIENT_PHONE_BY_ID).apply {
            setString(1, phone)
            setInt(2, clientId)
        }.executeUpdate()
        return when (rowsUpdated > 0) {
            true -> Unit
            false -> null
        }
    }

    override suspend fun changeEmail(clientId: Int, email: String): Unit? {
        val rowsUpdated = connection.prepareStatement(UPDATE_CLIENT_EMAIL_BY_ID).apply {
            setString(1, email)
            setInt(2, clientId)
        }.executeUpdate()
        return when (rowsUpdated > 0) {
            true -> Unit
            false -> null
        }
    }

    companion object {
        private const val SELECT_CLIENT_BY_ID = "SELECT * FROM ${Client.TABLE_NAME} WHERE ${Client.ID} = ?"

        private const val UPDATE_CLIENT_PHONE_BY_ID = "UPDATE ${Client.TABLE_NAME} SET ${Client.PHONE} = ?" +
                " WHERE ${Client.ID} = ?"

        private const val UPDATE_CLIENT_EMAIL_BY_ID = "UPDATE ${Client.TABLE_NAME} SET ${Client.EMAIL} = ?" +
                " WHERE ${Client.ID} = ?"
    }
}