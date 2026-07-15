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

    val isRegistrationOpen = MutableStateFlow<Boolean>(false)

    val supabaseSyncState = MutableStateFlow<String>("Checking configuration...")
    val isSupabaseConfigured = MutableStateFlow<Boolean>(false)

    init {
        try {
            val prefs = application.getSharedPreferences("ascl_prefs", Context.MODE_PRIVATE)
            isRegistrationOpen.value = prefs.getBoolean("is_registration_open", false)
            val jsonStr = prefs.getString("last_player_json", null)
            if (jsonStr != null) {
                val json = org.json.JSONObject(jsonStr)
                val player = PlayerRegistration(
                    id = json.optInt("id", 0),
                    uniquePlayerId = json.getString("uniquePlayerId"),
                    fullName = json.getString("fullName"),
                    nickname = if (json.has("nickname") && !json.isNull("nickname") && json.getString("nickname").isNotEmpty()) json.getString("nickname") else null,
                    dob = json.getString("dob"),
                    gender = if (json.has("gender")) json.getString("gender") else "Male",
                    phone = json.getString("phone"),
                    whatsapp = json.getString("whatsapp"),
                    email = json.getString("email"),
                    residentialArea = json.getString("residentialArea"),
                    experienceYears = json.getInt("experienceYears"),
                    preferredCueHand = json.getString("preferredCueHand"),
                    previousTournament = json.getString("previousTournament"),
                    skillLevel = json.getString("skillLevel"),
                    emergencyName = json.getString("emergencyName"),
                    emergencyRelationship = json.getString("emergencyRelationship"),
                    emergencyPhone = json.getString("emergencyPhone"),
                    passportPhotoUri = if (json.has("passportPhotoUri") && !json.isNull("passportPhotoUri") && json.getString("passportPhotoUri").isNotEmpty()) json.getString("passportPhotoUri") else null,
                    paymentProofUri = if (json.has("paymentProofUri") && !json.isNull("paymentProofUri") && json.getString("paymentProofUri").isNotEmpty()) json.getString("paymentProofUri") else null,
                    status = json.getString("status"),
                    registrationDate = json.getLong("registrationDate"),
                    referralCode = if (json.has("referralCode") && !json.isNull("referralCode")) json.getString("referralCode") else null
                )
                lastRegisteredPlayer.value = player
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        triggerSupabaseSync()
    }

    fun triggerSupabaseSync() {
        if (!com.example.data.SupabaseClient.isConfigured()) {
            isSupabaseConfigured.value = false
            supabaseSyncState.value = "Running in Offline Room Mode (Configure Supabase Secrets in AI Studio to Sync)"
            return
        }
        isSupabaseConfigured.value = true
        supabaseSyncState.value = "Syncing with Supabase..."
        viewModelScope.launch {
            val result = repository.syncWithSupabase()
            if (result.isSuccess) {
                supabaseSyncState.value = "Synced with Supabase successfully"
            } else {
                val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                supabaseSyncState.value = "Sync error: $errorMsg"
            }
        }
    }

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
        referralCode: String?,
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
                    paymentProofUri = paymentProofUri,
                    referralCode = referralCode
                )
                lastRegisteredPlayer.value = registeredPlayer
                try {
                    val prefs = getApplication<Application>().getSharedPreferences("ascl_prefs", Context.MODE_PRIVATE)
                    val json = org.json.JSONObject().apply {
                        put("id", registeredPlayer.id)
                        put("uniquePlayerId", registeredPlayer.uniquePlayerId)
                        put("fullName", registeredPlayer.fullName)
                        put("nickname", registeredPlayer.nickname ?: "")
                        put("dob", registeredPlayer.dob)
                        put("gender", registeredPlayer.gender)
                        put("phone", registeredPlayer.phone)
                        put("whatsapp", registeredPlayer.whatsapp)
                        put("email", registeredPlayer.email)
                        put("residentialArea", registeredPlayer.residentialArea)
                        put("experienceYears", registeredPlayer.experienceYears)
                        put("preferredCueHand", registeredPlayer.preferredCueHand)
                        put("previousTournament", registeredPlayer.previousTournament)
                        put("skillLevel", registeredPlayer.skillLevel)
                        put("emergencyName", registeredPlayer.emergencyName)
                        put("emergencyRelationship", registeredPlayer.emergencyRelationship)
                        put("emergencyPhone", registeredPlayer.emergencyPhone)
                        put("passportPhotoUri", registeredPlayer.passportPhotoUri ?: "")
                        put("paymentProofUri", registeredPlayer.paymentProofUri ?: "")
                        put("status", registeredPlayer.status)
                        put("registrationDate", registeredPlayer.registrationDate)
                        put("referralCode", registeredPlayer.referralCode ?: "")
                    }
                    prefs.edit().putString("last_player_json", json.toString()).apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                onSuccess(registeredPlayer)
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Registration failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun updateStatus(id: Int, status: String) {
        viewModelScope.launch {
            try {
                val player = registrations.value.find { it.id == id }
                repository.updateRegistrationStatus(id, status, player?.uniquePlayerId)
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

    fun setRegistrationOpen(open: Boolean) {
        isRegistrationOpen.value = open
        val prefs = getApplication<Application>().getSharedPreferences("ascl_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_registration_open", open).apply()
    }

    fun importPlayerToken(token: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                var rawToken = token.trim()
                if (rawToken.contains("import_token=")) {
                    rawToken = rawToken.substringAfter("import_token=").substringBefore("&")
                }
                // Decode from base64
                val decodedBytes = android.util.Base64.decode(rawToken, android.util.Base64.DEFAULT)
                val decodedString = String(decodedBytes, Charsets.UTF_8)
                val json = org.json.JSONObject(decodedString)

                val uniquePlayerId = json.getString("uniquePlayerId")
                
                // Avoid duplicates in the db
                val exists = registrations.value.any { 
                    it.uniquePlayerId == uniquePlayerId || 
                    (it.fullName.equals(json.getString("fullName"), ignoreCase = true) && 
                     it.email.equals(json.getString("email"), ignoreCase = true)) 
                }

                if (exists) {
                    onError("Athlete is already present on this device's roster.")
                    return@launch
                }

                val player = PlayerRegistration(
                    uniquePlayerId = uniquePlayerId,
                    fullName = json.getString("fullName"),
                    nickname = if (json.has("nickname") && !json.isNull("nickname")) json.getString("nickname") else null,
                    dob = json.getString("dob"),
                    gender = if (json.has("gender")) json.getString("gender") else "Male",
                    phone = if (json.has("phone")) json.getString("phone") else json.optString("whatsapp", ""),
                    whatsapp = json.getString("whatsapp"),
                    email = json.getString("email"),
                    residentialArea = json.getString("residentialArea"),
                    experienceYears = if (json.has("experienceYears")) json.getInt("experienceYears") else 2,
                    preferredCueHand = json.getString("preferredCueHand"),
                    previousTournament = if (json.has("previousTournament")) json.getString("previousTournament") else "",
                    skillLevel = json.getString("skillLevel"),
                    emergencyName = if (json.has("emergencyName")) json.getString("emergencyName") else "Emergency",
                    emergencyRelationship = if (json.has("emergencyRelationship")) json.getString("emergencyRelationship") else "Friend",
                    emergencyPhone = if (json.has("emergencyPhone")) json.getString("emergencyPhone") else "",
                    passportPhotoUri = if (json.has("passportPhotoUri")) json.optString("passportPhotoUri", null) else null,
                    paymentProofUri = if (json.has("paymentProofUri")) json.optString("paymentProofUri", null) else null,
                    status = if (json.has("status")) json.getString("status") else "Pending",
                    registrationDate = if (json.has("registrationDateLong")) json.getLong("registrationDateLong") else System.currentTimeMillis(),
                    referralCode = if (json.has("referralCode") && !json.isNull("referralCode") && json.getString("referralCode").isNotEmpty()) json.getString("referralCode") else null
                )

                repository.importPlayer(player)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Invalid transfer token layout.")
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
            val csvHeader = "ID,RegistrationDate,FullName,Nickname,DOB,Gender,Phone,WhatsApp,Email,Area,ExpYears,CueHand,PrevTournaments,Skill,EmergencyName,Relationship,EmergencyPhone,Status,ReferralCode\n"
            val csvBody = list.joinToString("\n") { r ->
                val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.US).format(java.util.Date(r.registrationDate))
                val nicknameSanitized = r.nickname?.replace("\"", "\"\"") ?: ""
                val prevTournSanitized = r.previousTournament.replace("\"", "\"\"")
                val referralSanitized = r.referralCode?.replace("\"", "\"\"") ?: ""
                
                "\"${r.uniquePlayerId}\",\"$date\",\"${r.fullName}\",\"$nicknameSanitized\",\"${r.dob}\",\"${r.gender}\",\"${r.phone}\",\"${r.whatsapp}\",\"${r.email}\",\"${r.residentialArea}\",${r.experienceYears},\"${r.preferredCueHand}\",\"$prevTournSanitized\",\"${r.skillLevel}\",\"${r.emergencyName}\",\"${r.emergencyRelationship}\",\"${r.emergencyPhone}\",\"${r.status}\",\"$referralSanitized\""
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
