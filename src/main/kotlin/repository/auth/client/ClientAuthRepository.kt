package com.andef.repository.auth.client

import com.andef.dto.auth.client.ClientAuthResponse
import kotlinx.datetime.LocalDate

interface ClientAuthRepository {
    suspend fun login(phone: String, password: String): ClientAuthResponse?
    suspend fun register(
        surname: String,
        name: String,
        phone: String,
        email: String,
        password: String,
        birthday: LocalDate,
        gender: String
    ): ClientAuthResponse?

    suspend fun passwordChange(phone: String, password: String): ClientAuthResponse?
}