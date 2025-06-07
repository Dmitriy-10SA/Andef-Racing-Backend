package com.andef.repository.booking.client

import com.andef.dto.booking.client.BookingDto
import com.andef.dto.booking.client.BookingStatusDto
import com.andef.dto.club.common.ClubDto
import com.andef.dto.club.common.CostDto
import com.andef.dto.club.common.PhotoDto
import com.andef.model.*
import kotlinx.datetime.*
import java.sql.Connection
import java.sql.Date
import java.sql.Time

class ClientBookingRepositoryImpl(private val connection: Connection) : ClientBookingRepository {
    override suspend fun getAllBookingsByClientId(clientId: Int): List<BookingDto> {
        val resultSet = connection.prepareStatement(SELECT_BOOKING_BY_CLIENT_ID).apply {
            setInt(1, clientId)
        }.executeQuery()
        return mutableListOf<BookingDto>().apply {
            while (resultSet.next()) {
                val bookingStatusId = resultSet.getInt(Booking.BOOKING_STATUS_ID)
                val costId = resultSet.getInt(Booking.COST_ID)
                val bookingStatusResultSet = connection.prepareStatement(
                    SELECT_BOOKING_STATUS_BY_STATUS_ID
                ).apply {
                    setInt(1, bookingStatusId)
                }.executeQuery()
                val costResultSet = connection.prepareStatement(SELECT_COST_BY_ID).apply {
                    setInt(1, costId)
                }.executeQuery()
                if (bookingStatusResultSet.next() && costResultSet.next()) {
                    add(
                        BookingDto(
                            id = resultSet.getInt(Booking.ID),
                            clientId = resultSet.getInt(Booking.CLIENT_ID),
                            clubId = resultSet.getInt(Booking.CLUB_ID),
                            cost = CostDto(
                                id = costId,
                                duration = costResultSet.getInt(Cost.DURATION),
                                value = costResultSet.getFloat(Cost.VALUE)
                            ),
                            bookingStatus = BookingStatusDto(
                                id = bookingStatusResultSet.getInt(BookingStatus.ID),
                                name = bookingStatusResultSet.getString(BookingStatus.NAME)
                            ),
                            date = resultSet.getDate(Booking.DATE).toLocalDate().toKotlinLocalDate(),
                            startTime = resultSet.getTime(Booking.START_TIME).toLocalTime().toKotlinLocalTime(),
                            endTime = resultSet.getTime(Booking.END_TIME).toLocalTime().toKotlinLocalTime()
                        )
                    )
                }
            }
        }.toList()
    }

    override suspend fun getClubByClubId(clubId: Int): ClubDto? {
        val resultSet = connection.prepareStatement(SELECT_CLUB_BY_ID).apply {
            setInt(1, clubId)
        }.executeQuery()
        return if (resultSet.next()) {
            val photosResultSet = connection.prepareStatement(SELECT_ALL_PHOTOS_BY_CLUB_ID).apply {
                setInt(1, clubId)
            }.executeQuery()
            val photos = mutableListOf<PhotoDto>().apply {
                while (photosResultSet.next()) {
                    add(
                        PhotoDto(
                            id = photosResultSet.getInt(ClubPhoto.ID),
                            link = photosResultSet.getString(ClubPhoto.LINK)
                        )
                    )
                }
            }.toList()
            ClubDto(
                id = clubId,
                name = resultSet.getString(Club.NAME),
                vkLink = resultSet.getString(Club.VK_LINK),
                telegramLink = resultSet.getString(Club.TELEGRAM_LINK),
                phone = resultSet.getString(Club.PHONE),
                address = resultSet.getString(Club.ADDRESS),
                numberOfEquipment = resultSet.getInt(Club.NUMBER_OF_EQUIPMENT),
                photos = photos
            )
        } else null
    }

    override suspend fun booking(
        clientId: Int,
        clubId: Int,
        costId: Int,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime
    ): Unit? {
        val intParams = mapOf(1 to clientId, 2 to clubId, 3 to costId, 4 to 1)
        val timeParams = mapOf(6 to startTime, 7 to endTime)
        val rowsInserted = connection.prepareStatement(INSERT_BOOKING).apply {
            intParams.forEach { i, param -> setInt(i, param) }
            setDate(5, Date.valueOf(date.toJavaLocalDate()))
            timeParams.forEach { i, param ->
                setTime(i, Time.valueOf(param.toJavaLocalTime()))
            }
        }.executeUpdate()
        return when (rowsInserted > 0) {
            true -> Unit
            false -> null
        }
    }

    override suspend fun getFreeTimesForDate(date: LocalDate): List<LocalTime> {
        val resultSet = connection.prepareStatement(SELECT_BOOKING_BY_DATE).apply {
            setDate(1, Date.valueOf(date.toString()))
        }.executeQuery()
        val bookingTimes = mutableSetOf<Int>().apply {
            while (resultSet.next()) {
                var startTime = resultSet.getTime(Booking.START_TIME).toLocalTime().toKotlinLocalTime()
                val endTime = resultSet.getTime(Booking.END_TIME).toLocalTime().toKotlinLocalTime()
                while (startTime.hour <= endTime.hour) {
                    add(startTime.hour)
                    startTime = startTime.toJavaLocalTime().plusHours(1).toKotlinLocalTime()
                }
            }
        }
        var time = LocalTime.parse("10:00")
        val endTime = LocalTime.parse("20:00")
        return mutableListOf<LocalTime>().apply {
            while (time.hour <= endTime.hour) {
                if (!bookingTimes.contains(time.hour)) {
                    add(time)
                }
                time = time.toJavaLocalTime().plusHours(1).toKotlinLocalTime()
            }
        }.toList()
    }

    override suspend fun bookingPay(bookingId: Int): Unit? {
        val rowsUpdated = connection.prepareStatement(UPDATE_BOOKING).apply {
            setInt(1, bookingId)
        }.executeUpdate()
        return when (rowsUpdated > 0) {
            true -> Unit
            false -> null
        }
    }

    companion object {
        private const val SELECT_BOOKING_BY_CLIENT_ID = "SELECT * FROM ${Booking.TABLE_NAME}" +
                " WHERE ${Booking.CLIENT_ID} = ?"
        private const val SELECT_BOOKING_STATUS_BY_STATUS_ID = "SELECT * FROM ${BookingStatus.TABLE_NAME} " +
                "WHERE ${BookingStatus.ID} = ?"
        private const val SELECT_COST_BY_ID = "SELECT * FROM ${Cost.TABLE_NAME} WHERE ${Cost.ID} = ?"
        private const val SELECT_CLUB_BY_ID = "SELECT * FROM ${Club.TABLE_NAME} WHERE ${Club.ID} = ?"
        private const val SELECT_ALL_PHOTOS_BY_CLUB_ID =
            "SELECT * FROM ${ClubPhoto.TABLE_NAME} WHERE ${ClubPhoto.CLUB_ID} = ?"
        private const val INSERT_BOOKING = "INSERT INTO ${Booking.TABLE_NAME} " +
                "(${Booking.CLIENT_ID}, ${Booking.CLUB_ID}, ${Booking.COST_ID}, ${Booking.BOOKING_STATUS_ID}," +
                " ${Booking.DATE}, ${Booking.START_TIME}, ${Booking.END_TIME}) VALUES (?, ?, ?, ?, ?, ?, ?)"
        private const val SELECT_BOOKING_BY_DATE = "SELECT * FROM ${Booking.TABLE_NAME} WHERE ${Booking.DATE} = ?"
        private const val UPDATE_BOOKING = "UPDATE ${Booking.TABLE_NAME} SET ${Booking.BOOKING_STATUS_ID} = 2 " +
                "WHERE ${Booking.ID} = ?"
    }
}