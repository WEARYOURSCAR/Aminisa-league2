package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Locale

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
        paymentProofUri: String?
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
            status = "Pending"
        )

        playerDao.insertRegistration(registration)
        return registration
    }

    suspend fun updateRegistrationStatus(id: Int, status: String) {
        playerDao.updateRegistrationStatus(id, status)
    }

    suspend fun importPlayer(registration: PlayerRegistration) {
        playerDao.insertRegistration(registration)
    }

    suspend fun getCount(): Int {
        return playerDao.getCount()
    }
}
