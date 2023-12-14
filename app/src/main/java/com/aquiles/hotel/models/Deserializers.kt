package com.aquiles.hotel.models

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class RoomDeserializer : JsonDeserializer<List<Room>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Room> {
        val rooms = mutableListOf<Room>()
        if (json is JsonArray) {
            for (jsonElement in json) {
                val room = context!!.deserialize<Room>(jsonElement, Room::class.java)
                rooms.add(room)
            }
        }
        return rooms
    }
}



class ReservationDeserializer : JsonDeserializer<List<Reservation>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Reservation> {
        val reservations = mutableListOf<Reservation>()
        if (json is JsonArray) {
            for (jsonElement in json) {
                val reservation = context!!.deserialize<Reservation>(jsonElement, Reservation::class.java)
                reservations.add(reservation)
            }
        }
        return reservations
    }
}



class ProfileDeserializer : JsonDeserializer<List<Profile>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Profile> {
        val profiles = mutableListOf<Profile>()
        if (json is JsonArray) {
            for (jsonElement in json) {
                val profile = context!!.deserialize<Profile>(jsonElement, Profile::class.java)
                profiles.add(profile)
            }
        }
        return profiles
    }
}


class ClientDeserializer : JsonDeserializer<List<Client>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Client> {
        val clients = mutableListOf<Client>()
        if (json is JsonArray) {
            for (jsonElement in json) {
                val client = context!!.deserialize<Client>(jsonElement, Client::class.java)
                clients.add(client)
            }
        }
        return clients
    }
}
