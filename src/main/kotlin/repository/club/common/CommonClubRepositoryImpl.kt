package com.andef.repository.club.common

import com.andef.dto.club.common.ClubDto
import com.andef.dto.club.common.CostDto
import com.andef.dto.club.common.EmployeeDto
import com.andef.dto.club.common.GameDto
import com.andef.model.Club
import com.andef.model.Cost
import com.andef.model.Employee
import com.andef.model.EmployeeType
import com.andef.model.Game
import java.sql.Connection

class CommonClubRepositoryImpl(private val connection: Connection) : CommonClubRepository {
    override suspend fun getAllClubs(): List<ClubDto> {
        val resultSet = connection.prepareStatement(SELECT_ALL_CLUBS).executeQuery()
        return mutableListOf<ClubDto>().apply {
            while (resultSet.next()) {
                add(
                    ClubDto(
                        id = resultSet.getInt(Club.ID),
                        name = resultSet.getString(Club.NAME),
                        vkLink = resultSet.getString(Club.VK_LINK),
                        telegramLink = resultSet.getString(Club.TELEGRAM_LINK),
                        phone = resultSet.getString(Club.PHONE),
                        address = resultSet.getString(Club.ADDRESS),
                        numberOfEquipment = resultSet.getInt(Club.NUMBER_OF_EQUIPMENT)
                    )
                )
            }
        }.toList()
    }

    override suspend fun getAllCostsInClub(clubId: Int): List<CostDto> {
        val resultSet = connection.prepareStatement(SELECT_ALL_COSTS_BY_CLUB_ID).apply {
            setInt(1, clubId)
        }.executeQuery()
        return mutableListOf<CostDto>().apply {
            while (resultSet.next()) {
                add(
                    CostDto(
                        id = resultSet.getInt(Cost.ID),
                        duration = resultSet.getInt(Cost.DURATION),
                        value = resultSet.getFloat(Cost.VALUE)
                    )
                )
            }
        }.toList()
    }

    override suspend fun getAllEmployeesInClub(clubId: Int): List<EmployeeDto> {
        val resultSet = connection.prepareStatement(SELECT_ALL_EMPLOYEES_BY_CLUB_ID).apply {
            setInt(1, clubId)
        }.executeQuery()
        return mutableListOf<EmployeeDto>().apply {
            while (resultSet.next()) {
                val employeeId = resultSet.getInt(Employee.ID)
                val statement = connection.prepareStatement(SELECT_EMPLOYEE_TYPE_NAME_BY_EMPLOYEE_TYPE_ID)
                val employeeTypeNameResultSet = statement.apply {
                    setInt(1, employeeId)
                }.executeQuery()
                if (employeeTypeNameResultSet.next()) {
                    add(
                        EmployeeDto(
                            id = employeeId,
                            employeeTypeName = employeeTypeNameResultSet.getString(EmployeeType.NAME),
                            surname = resultSet.getString(Employee.SURNAME),
                            name = resultSet.getString(Employee.NAME),
                            phone = resultSet.getString(Employee.PHONE)
                        )
                    )
                }
            }
        }.toList()
    }

    override suspend fun getAllGames(): List<GameDto> {
        val resultSet = connection.prepareStatement(SELECT_ALL_GAMES).executeQuery()
        return mutableListOf<GameDto>().apply {
            while (resultSet.next()) {
                add(
                    GameDto(
                        id = resultSet.getInt(Game.ID),
                        name = resultSet.getString(Game.NAME),
                        description = resultSet.getString(Game.DESCRIPTION)
                    )
                )
            }
        }.toList()
    }

    companion object {
        private const val SELECT_ALL_CLUBS = "SELECT * FROM ${Club.TABLE_NAME}"
        private const val SELECT_ALL_COSTS_BY_CLUB_ID = "SELECT * FROM ${Cost.TABLE_NAME} WHERE ${Cost.CLUB_ID} = ?"
        private const val SELECT_ALL_EMPLOYEES_BY_CLUB_ID = "SELECT * FROM ${Employee.TABLE_NAME} " +
                "WHERE ${Cost.CLUB_ID} = ?"
        private const val SELECT_EMPLOYEE_TYPE_NAME_BY_EMPLOYEE_TYPE_ID = "SELECT * FROM ${EmployeeType.TABLE_NAME}" +
                " WHERE ${EmployeeType.ID} = ?"
        private const val SELECT_ALL_GAMES = "SELECT * FROM ${Game.TABLE_NAME}"
    }
}