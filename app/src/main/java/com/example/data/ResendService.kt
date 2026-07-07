package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object ResendService {
    private const val TAG = "ResendService"
    private const val RESEND_API_URL = "https://api.resend.com/emails"
    private val client = OkHttpClient()

    fun isConfigured(): Boolean {
        return try {
            val key = BuildConfig.RESEND_API_KEY
            key.isNotEmpty() && !key.contains("your_api_key")
        } catch (e: Exception) {
            false
        }
    }

    suspend fun sendRegistrationEmail(
        recipientEmail: String,
        recipientName: String,
        uniqueId: String,
        status: String
    ): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured()) {
            Log.w(TAG, "Resend is not configured yet. Set RESEND_API_KEY in secrets.")
            return@withContext false
        }

        try {
            val apiKey = BuildConfig.RESEND_API_KEY
            val fromEmail = try {
                BuildConfig.RESEND_FROM_EMAIL.ifEmpty { "Aminisa Sport Club <onboarding@resend.dev>" }
            } catch (e: Exception) {
                "Aminisa Sport Club <onboarding@resend.dev>"
            }

            val subject = "Registration Confirmed: $uniqueId - Aminisa Sport Club League"
            
            val htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e5e5e5; border-radius: 12px; background-color: #0f0f0f; color: #ffffff;">
                    <div style="text-align: center; border-bottom: 2px solid #D4AF37; padding-bottom: 20px;">
                        <h1 style="color: #00A651; margin: 0; font-size: 24px; text-transform: uppercase; letter-spacing: 2px;">Aminisa Sport Club League</h1>
                        <p style="color: #D4AF37; margin: 5px 0 0 0; font-weight: bold; font-size: 14px;">OFFICIAL PLAYER ROSTER CONFIRMATION</p>
                    </div>
                    
                    <div style="padding: 20px 0; line-height: 1.6;">
                        <p style="font-size: 16px; margin-top: 0; color: #ffffff;">Hello <strong>$recipientName</strong>,</p>
                        <p style="font-size: 14px; color: #dddddd;">Congratulations! Your application has been successfully submitted and logged on the Aminisa Sport Club League digital roster.</p>
                        
                        <div style="background-color: #1a1a1a; border: 1px solid #333; padding: 15px; border-radius: 8px; margin: 20px 0; text-align: center;">
                            <span style="display: block; font-size: 11px; color: #888; text-transform: uppercase; letter-spacing: 1px;">Your Official Player ID</span>
                            <span style="font-size: 28px; font-weight: 900; color: #D4AF37; letter-spacing: 2px; display: block; margin: 5px 0;">$uniqueId</span>
                            <span style="display: inline-block; padding: 4px 12px; font-size: 11px; font-weight: bold; border-radius: 20px; background-color: rgba(212, 175, 55, 0.15); color: #D4AF37; border: 1px solid rgba(212, 175, 55, 0.3);">
                                STATUS: ${status.uppercase()}
                            </span>
                        </div>
                        
                        <h3 style="color: #00A651; border-bottom: 1px solid #222; padding-bottom: 5px; font-size: 15px;">What happens next?</h3>
                        <ul style="padding-left: 20px; font-size: 13px; color: #ccc;">
                            <li style="margin-bottom: 8px;"><strong>Roster Ledger Updates:</strong> Your player profile is now securely logged in our cloud registration system.</li>
                            <li style="margin-bottom: 8px;"><strong>Admin Evaluation:</strong> Our committee will review your submission documents, cue hand preference, skill level alignment, and registration fee receipt.</li>
                            <li style="margin-bottom: 8px;"><strong>Active Status:</strong> Once verified, your status will change to Approved, and you will be fully cleared to compete.</li>
                        </ul>
                        
                        <p style="font-size: 12px; color: #888; margin-top: 30px;">
                            This is an automated notification from the Aminisa Sport Club Portal. If you have any questions or need immediate support, you can reach out to the Admin team directly.
                        </p>
                    </div>
                    
                    <div style="border-top: 1px solid #222; padding-top: 20px; text-align: center; font-size: 11px; color: #555;">
                        <p style="margin: 0;">&copy; 2026 Aminisa Sport Club. All Rights Reserved.</p>
                    </div>
                </div>
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("from", fromEmail)
                put("to", JSONArray(listOf(recipientEmail)))
                put("subject", subject)
                put("html", htmlContent)
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(RESEND_API_URL)
                .addHeader("Authorization", "Bearer $apiKey")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string()
                if (response.isSuccessful) {
                    Log.d(TAG, "Email sent successfully to $recipientEmail: $responseBody")
                    true
                } else {
                    Log.e(TAG, "Failed to send email: Code ${response.code}, Body $responseBody")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending Resend email", e)
            false
        }
    }
}
