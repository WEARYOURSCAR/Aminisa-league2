package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_registrations")
data class PlayerRegistration(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uniquePlayerId: String,
    val registrationDate: Long = System.currentTimeMillis(),
    val fullName: String,
    val nickname: String? = null,
    val dob: String,
    val gender: String,
    val phone: String,
    val whatsapp: String,
    val email: String,
    val residentialArea: String,
    val experienceYears: Int,
    val preferredCueHand: String,
    val previousTournament: String,
    val skillLevel: String, // Beginner, Intermediate, Advanced
    val emergencyName: String,
    val emergencyRelationship: String,
    val emergencyPhone: String,
    val passportPhotoUri: String? = null,
    val paymentProofUri: String? = null,
    var status: String = "Pending" // Pending, Approved, Rejected
)
