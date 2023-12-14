package com.aquiles.hotel.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class New(

    val Nid : Int?,

    val  source : SourceModel?,

    val author: String?,

    val title :String?,

    val description : String?,

    val url: String?,
    val  urlToImage: String?,

    val publishedAt : String? = "",
)
