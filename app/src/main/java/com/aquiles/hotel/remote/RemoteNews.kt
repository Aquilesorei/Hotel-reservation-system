package com.aquiles.hotel.remote


import com.aquiles.hotel.data.SourceModel
import com.google.gson.annotations.SerializedName


data class RemoteNews(
    @SerializedName("source")
    val  source : SourceModel,
    @SerializedName("author")
      val author: String,
    @SerializedName("title")
      val title :String,
    @SerializedName("description")
      val description : String,
    @SerializedName("url")
       val url: String,
    @SerializedName("urlToImage")
    val  urlToImage: String,
    @SerializedName("publishedAt")
       val publishedAt : String = "",

    )