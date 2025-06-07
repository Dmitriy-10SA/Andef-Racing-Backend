package com.andef.dto.club.common

import kotlinx.serialization.Serializable

@Serializable
data class CostDto(
    val id: Int,
    val duration: Int,
    val value: Float
)
