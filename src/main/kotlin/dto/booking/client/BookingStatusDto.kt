package com.andef.dto.booking.client

import kotlinx.serialization.Serializable

@Serializable
data class BookingStatusDto(
    val id: Int,
    val name: String
)
