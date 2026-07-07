package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

@JsonClass(generateAdapter = true)
data class SupabaseRegistrationDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "uniquePlayerId") val uniquePlayerId: String,
    @Json(name = "registrationDate") val registrationDate: String,
    @Json(name = "fullName") val fullName: String,
    @Json(name = "nickname") val nickname: String? = null,
    @Json(name = "dob") val dob: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "whatsapp") val whatsapp: String,
    @Json(name = "email") val email: String,
    @Json(name = "residentialArea") val residentialArea: String,
    @Json(name = "experienceYears") val experienceYears: Int? = null,
    @Json(name = "preferredCueHand") val preferredCueHand: String,
    @Json(name = "previousTournament") val previousTournament: String? = null,
    @Json(name = "skillLevel") val skillLevel: String,
    @Json(name = "emergencyName") val emergencyName: String? = null,
    @Json(name = "emergencyRelationship") val emergencyRelationship: String? = null,
    @Json(name = "emergencyPhone") val emergencyPhone: String? = null,
    @Json(name = "passportPhotoUri") val passportPhotoUri: String? = null,
    @Json(name = "paymentProofUri") val paymentProofUri: String? = null,
    @Json(name = "status") val status: String,
    @Json(name = "referralCode") val referralCode: String? = null,
    @Json(name = "passportData") val passportData: String? = null,
    @Json(name = "receiptData") val receiptData: String? = null,
    @Json(name = "hashValue") val hashValue: Long? = null
)

interface SupabaseApi {
    @GET("rest/v1/player_registrations")
    suspend fun getRegistrations(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Query("select") select: String = "*"
    ): List<SupabaseRegistrationDto>

    @POST("rest/v1/player_registrations")
    suspend fun insertRegistration(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Body registration: SupabaseRegistrationDto
    ): List<SupabaseRegistrationDto>

    @PATCH("rest/v1/player_registrations")
    suspend fun updateRegistrationStatus(
        @Header("apikey") apiKey: String,
        @Header("Authorization") auth: String,
        @Query("uniquePlayerId") uniquePlayerId: String,
        @Body body: Map<String, String>
    ): Response<Unit>
}

object SupabaseClient {
    private var apiInstance: SupabaseApi? = null

    fun isConfigured(): Boolean {
        return try {
            val url = BuildConfig.SUPABASE_URL
            val key = BuildConfig.SUPABASE_ANON_KEY
            url.isNotEmpty() && !url.contains("your-project") && key.isNotEmpty() && !key.contains("your-anon-key")
        } catch (e: Exception) {
            false
        }
    }

    fun getApi(): SupabaseApi? {
        if (!isConfigured()) return null
        if (apiInstance == null) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            val baseUrl = if (BuildConfig.SUPABASE_URL.endsWith("/")) BuildConfig.SUPABASE_URL else "${BuildConfig.SUPABASE_URL}/"

            apiInstance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(SupabaseApi::class.java)
        }
        return apiInstance
    }
}
