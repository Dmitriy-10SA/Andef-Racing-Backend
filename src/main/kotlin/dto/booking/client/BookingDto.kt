package com.andef.dto.booking.client

import com.andef.dto.club.common.CostDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    val id: Int,
    val clientId: Int,
    val clubId: Int,
    val cost: CostDto,
    val bookingStatus: BookingStatusDto,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime
)
