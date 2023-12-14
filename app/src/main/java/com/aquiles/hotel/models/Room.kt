package com.aquiles.hotel.models


/**
 * @project IntelliJ IDEA
 * @author aquiles on 4/17/23 6:30 PM
 */
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class Room(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?= null,
    @ColumnInfo(name = "capacity")
    val capacity: Int,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "is_available")
    val is_available: Boolean
){

    companion object  {
        fun new() =Room(null,0,"",0,false)
    }
}


