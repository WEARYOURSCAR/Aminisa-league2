package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM player_registrations ORDER BY registrationDate DESC")
    fun getAllRegistrations(): Flow<List<PlayerRegistration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: PlayerRegistration): Long

    @Update
    suspend fun updateRegistration(registration: PlayerRegistration)

    @Query("UPDATE player_registrations SET status = :status WHERE id = :id")
    suspend fun updateRegistrationStatus(id: Int, status: String)

    @Query("SELECT COUNT(*) FROM player_registrations")
    suspend fun getCount(): Int

    @Query("SELECT MAX(id) FROM player_registrations")
    suspend fun getMaxId(): Int?
}
