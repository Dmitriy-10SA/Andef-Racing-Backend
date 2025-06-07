package com.andef.dto.club.common

import kotlinx.serialization.Serializable

@Serializable
data class ClubDto(
    val id: Int,
    val name: String,
    val vkLink: String,
    val telegramLink: String,
    val phone: String,
    val address: String,
    val numberOfEquipment: Int,
    val photos: List<PhotoDto>
)
