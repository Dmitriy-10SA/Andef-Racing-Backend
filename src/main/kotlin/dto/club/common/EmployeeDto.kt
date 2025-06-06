package com.andef.dto.club.common

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeDto(
    val id: Int,
    val employeeTypeName: String,
    val surname: String,
    val name: String,
    val phone: String
)
