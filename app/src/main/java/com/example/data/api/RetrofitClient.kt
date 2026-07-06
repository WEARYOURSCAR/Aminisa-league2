package com.example.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit Client configured with OkHttpClient logging interceptors.
 * Safe compiled fallback prevents errors if BASE_URL is not defined in BuildConfig.
 */
object RetrofitClient {
    // Default production domain for ASCL API endpoints.
    private const val DEFAULT_BASE_URL = "https://aminisasport.com.ng/"

    val resendService: ResendService by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        val baseUrl = try {
            // Use reflection to check if BASE_URL was exposed by gradle-secrets-plugin from your .env
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("BASE_URL")
            val value = field.get(null) as? String
            
            if (!value.isNullOrBlank()) {
                if (value.endsWith("/")) value else "$value/"
            } else {
                DEFAULT_BASE_URL
            }
        } catch (e: Exception) {
            DEFAULT_BASE_URL
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ResendService::class.java)
    }
}
