package com.aquiles.hotel.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * @project IntelliJ IDEA
 * @author aquiles on 4/17/23 6:30 PM
 */

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?= null,
    @ColumnInfo(name = "first_name")
    val first_name :String,
    @ColumnInfo(name = "last_name")
    val last_name : String,
    @ColumnInfo(name = "age")
    val age :Int,
    @ColumnInfo(name = "email")
    val email : String,
    @ColumnInfo(name = "profile_picture")
    var profil_picture : String,
    @ColumnInfo(name = "password")
    val  password : String
)
