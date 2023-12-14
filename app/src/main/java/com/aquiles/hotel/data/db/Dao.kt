package com.aquiles.hotel.data

import androidx.room.*
import com.aquiles.hotel.local.LocalNews
import com.aquiles.hotel.models.*
import com.aquiles.hotel.models.Room

@Dao
interface HotDao {

    @Query("SELECT * FROM profiles")
    suspend fun getAllProfiles() : List<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile)



    @Delete
    suspend fun removeProfile(profile: Profile)

    @Query("DELETE FROM profiles")
    suspend fun  removeAllProfiles()


    @Query("SELECT * FROM rooms")
    suspend fun getAllRooms() : List<Room>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: Room)

    @Query("SELECT * FROM rooms WHERE id = :mid")
    suspend fun  getRoomById(mid :Int) : Room?

    @Update(entity = Room::class)
    suspend fun  updateRoom(room: Room)

    @Delete
    suspend fun removeRoom(room: Room)

    @Query("DELETE FROM rooms")
    suspend fun  removeAllRooms()


    @Query("SELECT * FROM reservations")
    suspend fun getAllReservations() : List<Reservation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Delete
    suspend fun removeReservation(reservation: Reservation)

    @Query("DELETE FROM reservations")
    suspend fun  removeAllReservations()

    @Query("SELECT * FROM clients")
    suspend fun getAllClients() : List<Client>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun removeClient(client: Client)

    @Query("DELETE FROM clients")
    suspend fun  removeAllClients()


    @Query("SELECT * FROM news")
    fun getAll() : List<LocalNews>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(notes : List<LocalNews>)

    @Query("SELECT * from news where Nid = :id")
    fun getById(id: Int) : LocalNews?

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

}