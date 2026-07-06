package com.example.data.api

import com.squareup.moshi.JsonClass

/**
 * Request body model for Sending Welcome Email via ASCL api/welcome function.
 */
@JsonClass(generateAdapter = true)
data class WelcomeEmailRequest(
    val email: String,
    val fullName: String,
    val uniquePlayerId: String
)

/**
 * Response model from ASCL api/welcome function.
 */
@JsonClass(generateAdapter = true)
data class WelcomeEmailResponse(
    val success: Boolean,
    val id: String? = null,
    val error: String? = null
)
