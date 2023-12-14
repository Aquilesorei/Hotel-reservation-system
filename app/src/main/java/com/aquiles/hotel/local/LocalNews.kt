package com.aquiles.hotel.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "news")
data class LocalNews(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Nid")
    val Nid : Int?,
    @ColumnInfo(name ="source_name")
    val  source_name : String?,
    @ColumnInfo(name ="source_id")
    val  source_id : String?,
    @ColumnInfo(name ="author")
    val author: String?,
    @ColumnInfo(name ="title")
    val title :String?,
    @ColumnInfo(name ="description")
    val description : String?,
    @ColumnInfo(name ="url")
    val url: String?,
    @ColumnInfo(name ="urlToImage")
    val  urlToImage: String?,
    @ColumnInfo(name ="publishedAt")
    val publishedAt : String? = "",
)

