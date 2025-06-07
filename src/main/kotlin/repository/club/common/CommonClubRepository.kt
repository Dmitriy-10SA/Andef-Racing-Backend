package com.andef.repository.club.common

import com.andef.dto.club.common.ClubDto
import com.andef.dto.club.common.CostDto
import com.andef.dto.club.common.EmployeeDto
import com.andef.dto.club.common.GameDto

interface CommonClubRepository {
    suspend fun getAllClubs(): List<ClubDto>
    suspend fun getAllCostsInClub(clubId: Int): List<CostDto>
    suspend fun getAllEmployeesInClub(clubId: Int): List<EmployeeDto>
    suspend fun getAllGames(): List<GameDto>
}