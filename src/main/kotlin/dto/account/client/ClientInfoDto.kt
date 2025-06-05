package com.andef.dto.account.client

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class ClientInfoDto(
    val surname: String,
    val name: String,
    val phone: String,
    val email: String,
    val birthday: LocalDate,
    val gender: String
)
