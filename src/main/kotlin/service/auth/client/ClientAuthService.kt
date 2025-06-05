package com.andef.service.auth.client

import com.andef.dto.auth.client.ClientAuthResponse
import com.andef.dto.auth.client.ClientLoginRequest
import com.andef.dto.auth.client.ClientPasswordChangeRequest
import com.andef.dto.auth.client.ClientRegisterRequest
import com.andef.repository.auth.client.ClientAuthRepository
import com.andef.repository.auth.client.ClientAuthRepositoryImpl
import java.sql.Connection

class ClientAuthService(
    private val connection: Connection,
    private val repository: ClientAuthRepository = ClientAuthRepositoryImpl(connection)
) {
    fun login(clientLoginRequest: ClientLoginRequest): ClientAuthResponse? {
        val phone = clientLoginRequest.phone
        val password = clientLoginRequest.password
        return repository.login(phone, password)
    }

    fun register(clientRegisterRequest: ClientRegisterRequest): ClientAuthResponse? {
        val surname = clientRegisterRequest.surname
        val name = clientRegisterRequest.name
        val phone = clientRegisterRequest.phone
        val email = clientRegisterRequest.email
        val password = clientRegisterRequest.password
        return repository.register(surname, name, phone, email, password)
    }

    fun passwordChange(clientPasswordChangeRequest: ClientPasswordChangeRequest): ClientAuthResponse? {
        val phone = clientPasswordChangeRequest.phone
        val password = clientPasswordChangeRequest.password
        return repository.passwordChange(phone, password)
    }
}