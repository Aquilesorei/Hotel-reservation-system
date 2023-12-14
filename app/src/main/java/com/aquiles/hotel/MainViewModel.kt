package com.aquiles.hotel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aquiles.hotel.data.Repository
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.models.Reservation
import com.aquiles.hotel.models.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainViewModel : ViewModel() {
    var profile = mutableStateOf(Profile(0,"","",0,"","",""))
    var rooms = SnapshotStateList<Room>()

    var clients = SnapshotStateList<Client>()
     var reservations =SnapshotStateList<Reservation>()



    init {
        updateRooms()
        updateClients()
        updateReservations()
    }
    fun  updateRooms(){
        viewModelScope.launch(Dispatchers.IO) {
            val cl= Repository.getAllRooms()
            withContext(Dispatchers.Main) {
                rooms.clear()
                rooms.addAll(cl)
            }
        }
    }


    fun  updateClients(){
        viewModelScope.launch(Dispatchers.IO) {
            val cl= Repository.getAllClients()
            withContext(Dispatchers.Main) {
                clients.clear()
                clients.addAll(cl)
            }
        }
    }


    fun  updateReservations(){
        viewModelScope.launch(Dispatchers.IO) {
            val cl= Repository.getAllReservations()
            withContext(Dispatchers.Main) {
                reservations.clear()
                reservations.addAll(cl)
            }
        }
    }



}