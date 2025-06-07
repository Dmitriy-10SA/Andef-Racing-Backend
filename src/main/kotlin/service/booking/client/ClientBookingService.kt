package com.andef.service.booking.client

import com.andef.dto.booking.client.BookingRequestDto
import com.andef.repository.booking.client.ClientBookingRepository
import com.andef.repository.booking.client.ClientBookingRepositoryImpl
import kotlinx.datetime.LocalDate
import java.sql.Connection

class ClientBookingService(
    private val connection: Connection,
    private val repository: ClientBookingRepository = ClientBookingRepositoryImpl(connection)
) {
    suspend fun getAllBookingByClientId(clientId: Int) = repository.getAllBookingsByClientId(clientId)
    suspend fun getClubByClubId(clubId: Int) = repository.getClubByClubId(clubId)
    suspend fun booking(clientId: Int, bookingRequestDto: BookingRequestDto) = repository.booking(
        clientId = clientId,
        clubId = bookingRequestDto.clubId,
        costId = bookingRequestDto.costId,
        date = bookingRequestDto.date,
        startTime = bookingRequestDto.startTime,
        endTime = bookingRequestDto.endTime
    )
    suspend fun getFreeTimesForDate(date: LocalDate) = repository.getFreeTimesForDate(date)
    suspend fun bookingPay(bookingId: Int) = repository.bookingPay(bookingId)
}