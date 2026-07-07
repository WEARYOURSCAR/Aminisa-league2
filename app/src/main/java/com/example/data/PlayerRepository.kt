package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Locale
import com.example.BuildConfig

class PlayerRepository(private val playerDao: PlayerDao) {
    val allRegistrations: Flow<List<PlayerRegistration>> = playerDao.getAllRegistrations()

    suspend fun registerPlayer(
        fullName: String,
        nickname: String?,
        dob: String,
        gender: String,
        phone: String,
        whatsapp: String,
        email: String,
        residentialArea: String,
        experienceYears: Int,
        preferredCueHand: String,
        previousTournament: String,
        skillLevel: String,
        emergencyName: String,
        emergencyRelationship: String,
        emergencyPhone: String,
        passportPhotoUri: String?,
        paymentProofUri: String?,
        referralCode: String?
    ): PlayerRegistration {
        val maxId = playerDao.getMaxId() ?: 0
        val nextIdNum = maxId + 1
        val uniqueId = String.format(Locale.US, "ASC-%04d", nextIdNum)

        val registration = PlayerRegistration(
            uniquePlayerId = uniqueId,
            fullName = fullName,
            nickname = nickname,
            dob = dob,
            gender = gender,
            phone = phone,
            whatsapp = whatsapp,
            email = email,
            residentialArea = residentialArea,
            experienceYears = experienceYears,
            preferredCueHand = preferredCueHand,
            previousTournament = previousTournament,
            skillLevel = skillLevel,
            emergencyName = emergencyName,
            emergencyRelationship = emergencyRelationship,
            emergencyPhone = emergencyPhone,
            passportPhotoUri = passportPhotoUri,
            paymentProofUri = paymentProofUri,
            status = "Pending",
            referralCode = referralCode
        )

        playerDao.insertRegistration(registration)

        // Sync to Supabase if configured
        if (SupabaseClient.isConfigured()) {
            try {
                val api = SupabaseClient.getApi()
                val key = BuildConfig.SUPABASE_ANON_KEY
                val auth = "Bearer $key"
                api?.insertRegistration(key, auth, registration = registration.toDto())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Send confirmation email via Resend if configured
        if (ResendService.isConfigured() && registration.email.isNotEmpty()) {
            try {
                ResendService.sendRegistrationEmail(
                    recipientEmail = registration.email,
                    recipientName = registration.fullName,
                    uniqueId = registration.uniquePlayerId,
                    status = registration.status
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return registration
    }

    suspend fun updateRegistrationStatus(id: Int, status: String, uniquePlayerId: String? = null) {
        playerDao.updateRegistrationStatus(id, status)

        // Update status in Supabase if configured
        if (SupabaseClient.isConfigured() && uniquePlayerId != null) {
            try {
                val api = SupabaseClient.getApi()
                val key = BuildConfig.SUPABASE_ANON_KEY
                val auth = "Bearer $key"
                api?.updateRegistrationStatus(
                    apiKey = key,
                    auth = auth,
                    uniquePlayerId = "eq.$uniquePlayerId",
                    body = mapOf("status" to status)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun importPlayer(registration: PlayerRegistration) {
        playerDao.insertRegistration(registration)
    }

    suspend fun getCount(): Int {
        return playerDao.getCount()
    }

    suspend fun syncWithSupabase(): Result<Unit> {
        if (!SupabaseClient.isConfigured()) {
            return Result.failure(Exception("Supabase is not configured yet. Configure SUPABASE_URL and SUPABASE_ANON_KEY in your secrets."))
        }
        return try {
            val api = SupabaseClient.getApi() ?: return Result.failure(Exception("Could not initialize Supabase API client"))
            val key = BuildConfig.SUPABASE_ANON_KEY
            val auth = "Bearer $key"
            val remoteDtos = api.getRegistrations(key, auth)
            val localList = playerDao.getAllRegistrationsOnce()

            // 1. Sync remote into local Room database
            for (remote in remoteDtos) {
                val local = localList.find { it.uniquePlayerId == remote.uniquePlayerId }
                if (local == null) {
                    playerDao.insertRegistration(remote.toEntity())
                } else if (local.status != remote.status) {
                    playerDao.updateRegistrationStatus(local.id, remote.status)
                }
            }

            // 2. Sync local-only offline registrations to remote Supabase
            for (local in localList) {
                val remoteExists = remoteDtos.any { it.uniquePlayerId == local.uniquePlayerId }
                if (!remoteExists) {
                    try {
                        api.insertRegistration(key, auth, registration = local.toDto())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Conversion helper extensions
    private fun PlayerRegistration.toDto(): SupabaseRegistrationDto {
        return SupabaseRegistrationDto(
            uniquePlayerId = this.uniquePlayerId,
            registrationDate = this.registrationDate.toString(),
            fullName = this.fullName,
            nickname = this.nickname,
            dob = this.dob,
            gender = this.gender,
            phone = this.phone,
            whatsapp = this.whatsapp,
            email = this.email,
            residentialArea = this.residentialArea,
            experienceYears = this.experienceYears,
            preferredCueHand = this.preferredCueHand,
            previousTournament = this.previousTournament,
            skillLevel = this.skillLevel,
            emergencyName = this.emergencyName,
            emergencyRelationship = this.emergencyRelationship,
            emergencyPhone = this.emergencyPhone,
            passportPhotoUri = this.passportPhotoUri,
            paymentProofUri = this.paymentProofUri,
            status = this.status,
            referralCode = this.referralCode
        )
    }

    private fun SupabaseRegistrationDto.toEntity(): PlayerRegistration {
        val regDateLong = try {
            this.registrationDate.toLong()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
        return PlayerRegistration(
            uniquePlayerId = this.uniquePlayerId,
            registrationDate = regDateLong,
            fullName = this.fullName,
            nickname = this.nickname,
            dob = this.dob,
            gender = this.gender,
            phone = this.phone ?: "",
            whatsapp = this.whatsapp,
            email = this.email,
            residentialArea = this.residentialArea,
            experienceYears = this.experienceYears ?: 0,
            preferredCueHand = this.preferredCueHand,
            previousTournament = this.previousTournament ?: "",
            skillLevel = this.skillLevel,
            emergencyName = this.emergencyName ?: "",
            emergencyRelationship = this.emergencyRelationship ?: "",
            emergencyPhone = this.emergencyPhone ?: "",
            passportPhotoUri = this.passportPhotoUri ?: this.passportData,
            paymentProofUri = this.paymentProofUri ?: this.receiptData,
            status = this.status,
            referralCode = this.referralCode
        )
    }
}
