package com.andef.dto.auth.client

import kotlinx.serialization.Serializable

@Serializable
data class ClientAuthResponse(val token: String)
