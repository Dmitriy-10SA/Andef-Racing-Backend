package com.andef.service.club.common

import com.andef.repository.club.common.CommonClubRepository
import com.andef.repository.club.common.CommonClubRepositoryImpl
import java.sql.Connection

class CommonClubService(
    private val connection: Connection,
    private val repository: CommonClubRepository = CommonClubRepositoryImpl(connection)
) {
    suspend fun getAllClubs() = repository.getAllClubs()
    suspend fun getAllCostsInClub(clubId: Int) = repository.getAllCostsInClub(clubId)
    suspend fun getAllEmployeesInClub(clubId: Int) = repository.getAllEmployeesInClub(clubId)
}