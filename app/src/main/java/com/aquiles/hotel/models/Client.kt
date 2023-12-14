package com.aquiles.hotel.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @project IntelliJ IDEA
 * @author aquiles on 4/17/23 8:47 PM
 */
@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?= null,
    @ColumnInfo(name = "name")
    val name: String,
)
