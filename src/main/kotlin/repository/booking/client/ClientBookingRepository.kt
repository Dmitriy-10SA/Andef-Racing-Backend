package com.andef.repository.booking.client

import com.andef.dto.booking.client.BookingDto
import com.andef.dto.club.common.ClubDto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface ClientBookingRepository {
    suspend fun getAllBookingsByClientId(clientId: Int): List<BookingDto>
    suspend fun getClubByClubId(clubId: Int): ClubDto?
    suspend fun booking(
        clientId: Int,
        clubId: Int,
        costId: Int,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): Unit?

    suspend fun getFreeTimesForDate(date: LocalDate): List<LocalTime>
    suspend fun bookingPay(bookingId: Int): Unit?
}