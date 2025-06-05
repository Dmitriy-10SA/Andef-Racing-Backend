package com.andef.dto.auth.client

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ClientLoginRequest(
    val phone: String,
    val password: String
)

@Serializable
data class ClientPasswordChangeRequest(
    val phone: String,
    val password: String
)

@Serializable
data class ClientRegisterRequest(
    val surname: String,
    val name: String,
    val phone: String,
    val email: String,
    val password: String,
    val birthday: LocalDate,
    val gender: String
)