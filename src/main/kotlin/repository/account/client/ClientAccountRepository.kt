package com.andef.repository.account.client

import com.andef.dto.account.client.ClientInfoDto

interface ClientAccountRepository {
    suspend fun getInfo(clientId: Int): ClientInfoDto?
    suspend fun changePhone(clientId: Int, phone: String): Unit?
    suspend fun changeEmail(clientId: Int, email: String): Unit?
}