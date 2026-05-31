package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PlayerRegistration
import com.example.data.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeagueViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = PlayerRepository(db.playerDao())

    val registrations: StateFlow<List<PlayerRegistration>> = repository.allRegistrations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val searchQuery = MutableStateFlow("")

    val filteredRegistrations: StateFlow<List<PlayerRegistration>> = combine(
        registrations,
        searchQuery
    ) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.fullName.contains(query, ignoreCase = true) ||
                        it.uniquePlayerId.contains(query, ignoreCase = true) ||
                        (it.nickname?.contains(query, ignoreCase = true) ?: false) ||
                        it.email.contains(query, ignoreCase = true) ||
                        it.phone.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Last registered player to display on success screen
    val lastRegisteredPlayer = MutableStateFlow<PlayerRegistration?>(null)

    // Player statistics
    val totalPlayersCount: StateFlow<Int> = registrations
        .combine(registrations) { list, _ -> list.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingCount: StateFlow<Int> = registrations
        .combine(registrations) { list, _ -> list.count { it.status == "Pending" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val approvedCount: StateFlow<Int> = registrations
        .combine(registrations) { list, _ -> list.count { it.status == "Approved" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val rejectedCount: StateFlow<Int> = registrations
        .combine(registrations) { list, _ -> list.count { it.status == "Rejected" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun registerPlayer(
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
        onSuccess: (PlayerRegistration) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val registeredPlayer = repository.registerPlayer(
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
                    paymentProofUri = paymentProofUri
                )
                lastRegisteredPlayer.value = registeredPlayer
                onSuccess(registeredPlayer)
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Registration failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            try {
                repository.updateRegistrationStatus(id, status)
                // Also update the state of lastRegisteredPlayer if it's the same player
                val currentLast = lastRegisteredPlayer.value
                if (currentLast != null && currentLast.id == id) {
                    lastRegisteredPlayer.value = currentLast.copy(status = status)
                }
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Failed to update status", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun downloadRegistrationsAsCsv(context: Context) {
        val list = registrations.value
        if (list.isEmpty()) {
            Toast.makeText(context, "No registrations to export", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val csvHeader = "ID,RegistrationDate,FullName,Nickname,DOB,Gender,Phone,WhatsApp,Email,Area,ExpYears,CueHand,PrevTournaments,Skill,EmergencyName,Relationship,EmergencyPhone,Status\n"
            val csvBody = list.joinToString("\n") { r ->
                val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.US).format(java.util.Date(r.registrationDate))
                val nicknameSanitized = r.nickname?.replace("\"", "\"\"") ?: ""
                val prevTournSanitized = r.previousTournament.replace("\"", "\"\"")
                
                "\"${r.uniquePlayerId}\",\"$date\",\"${r.fullName}\",\"$nicknameSanitized\",\"${r.dob}\",\"${r.gender}\",\"${r.phone}\",\"${r.whatsapp}\",\"${r.email}\",\"${r.residentialArea}\",${r.experienceYears},\"${r.preferredCueHand}\",\"$prevTournSanitized\",\"${r.skillLevel}\",\"${r.emergencyName}\",\"${r.emergencyRelationship}\",\"${r.emergencyPhone}\",\"${r.status}\""
            }
            val csvString = csvHeader + csvBody

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT, "ASCL_Registrations.csv")
                putExtra(Intent.EXTRA_TEXT, csvString)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Save or Export Registrations CSV"))
            Toast.makeText(context, "Exporting CSV...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Export error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
