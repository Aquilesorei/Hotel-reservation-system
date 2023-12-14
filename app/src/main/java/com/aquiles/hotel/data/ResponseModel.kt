package com.aquiles.hotel.data

import com.aquiles.hotel.remote.RemoteNews
import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("status")
   val status :String,
    @SerializedName("totalResults")
   val totalResults : Int,
    @SerializedName("articles")
 val articles:  List<RemoteNews>,
)
