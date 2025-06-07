package com.andef.dto.booking.client

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class BookingRequestDto(
    val clubId: Int,
    val costId: Int,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
