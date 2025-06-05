package com.andef.repository.auth.client

import com.andef.dto.auth.client.ClientAuthResponse

interface ClientAuthRepository {
    fun login(phone: String, password: String): ClientAuthResponse?
    fun register(surname: String, name: String, phone: String, email: String, password: String): ClientAuthResponse?
    fun passwordChange(phone: String, password: String): ClientAuthResponse?
}