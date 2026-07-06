package com.example.data.api

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Service description mapping to Vercel Serverless proxy backend.
 */
interface ResendService {
    @POST("api/welcome")
    suspend fun sendWelcomeEmail(
        @Body request: WelcomeEmailRequest
    ): WelcomeEmailResponse
}
