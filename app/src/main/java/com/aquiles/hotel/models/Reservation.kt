package com.aquiles.hotel.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @project IntelliJ IDEA
 * @author aquiles on 4/17/23 6:31 PM
 */
@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?= null,
    @ColumnInfo(name = "client_name")
    val client_name: String,
    @ColumnInfo(name = "room_id")
    val room_id: String,
    @ColumnInfo(name = "start_date")
    val start_date: String,
    @ColumnInfo(name = "end_date")
    val end_date: String,
    @ColumnInfo(name = "total_price")
    val total_price: Double,
    @ColumnInfo(name = "is_validated")
    val is_validated: Boolean
){
    companion object{
        fun new() = Reservation(null,"","","","",0.0,false)
    }
}
