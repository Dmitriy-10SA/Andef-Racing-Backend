package com.andef.dto.club.common

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDto(
    val id: Int,
    val link: String
)
