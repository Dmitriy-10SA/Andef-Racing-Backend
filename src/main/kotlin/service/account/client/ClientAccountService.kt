package com.andef.service.account.client

import com.andef.repository.account.client.ClientAccountRepository
import com.andef.repository.account.client.ClientAccountRepositoryImpl
import java.sql.Connection

class ClientAccountService(
    private val connection: Connection,
    private val repository: ClientAccountRepository = ClientAccountRepositoryImpl(connection)
) {
    suspend fun getInfo(clientId: Int) = repository.getInfo(clientId)
    suspend fun changeEmail(clientId: Int, email: String) = repository.changeEmail(clientId, email)
    suspend fun changePhone(clientId: Int, phone: String) = repository.changePhone(clientId, phone)
}