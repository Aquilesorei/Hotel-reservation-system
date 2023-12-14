package com.aquiles.hotel.data

import android.hardware.usb.UsbEndpoint
import android.util.Log
import com.aquiles.hotel.HotApplication
import com.aquiles.hotel.data.db.HotDb
import com.aquiles.hotel.getServerIp
import com.aquiles.hotel.libs.newsapilib.NewsApiClient
import com.aquiles.hotel.libs.newsapilib.models.request.TopHeadlinesRequest
import com.aquiles.hotel.libs.newsapilib.models.response.ArticleResponse
import com.aquiles.hotel.local.LocalNews
import com.aquiles.hotel.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.ConnectionPool
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

object Repository {


    val scope = CoroutineScope(Dispatchers.IO)
    private val hotDao = HotDb.getDaoInstance(HotApplication.getAppContext())


    init {

    }

    suspend fun getAllProfiles() : List<Profile> {
        return hotDao.getAllProfiles()
    }




    suspend fun insertProfile(profile: Profile) {
        hotDao.insertProfile(profile)
    }

    suspend fun removeProfile(profile: Profile) {
        hotDao.removeProfile(profile)
    }

    suspend fun removeAllProfiles() {
        hotDao.removeAllProfiles()
    }

    suspend fun getAllRooms() : List<Room> {
        return hotDao.getAllRooms()
    }

    suspend fun  getRoom(id : Int) = hotDao.getRoomById(id)

    suspend fun insertRoom(room: Room) {
        hotDao.insertRoom(room)
    }

    suspend fun updateRoom(room: Room) = hotDao.updateRoom(room)
    suspend fun removeRoom(room: Room) {
        hotDao.removeRoom(room)
    }

    suspend fun removeAllRooms() {
        hotDao.removeAllRooms()
    }

    suspend fun getAllReservations() : List<Reservation> {
        return hotDao.getAllReservations()
    }

    suspend fun  getLastReservation(): Reservation? {
        val allReservations  = getAllReservations()
        return if(allReservations.isEmpty()){
            null
        }
        else
        {
            allReservations.last()
        }

    }

    suspend fun insertReservation(reservation: Reservation) {
        hotDao.insertReservation(reservation)
    }

    suspend fun removeReservation(reservation: Reservation) {
        hotDao.removeReservation(reservation)
    }

    suspend fun removeAllReservations() {
        hotDao.removeAllReservations()
    }

    suspend fun getAllClients() : List<Client> {
        return hotDao.getAllClients()
    }

    suspend fun insertClient(client: Client) {
        return hotDao.insertClient(client)
    }

    suspend fun removeClient(client: Client) {
        return hotDao.removeClient(client)
    }

    suspend fun removeAllClients() {
        return hotDao.removeAllClients()
    }




    suspend fun getNews() : List<New> {
        return withContext(Dispatchers.IO) {
            return@withContext hotDao.getAll().map {
                New(
                    Nid = it.Nid,
                    source = SourceModel(it.source_id,it.source_name),
                    author = it.author,
                    title =  it.title,
                    description =  it.description,
                    urlToImage =  it.urlToImage,
                    url = it.url,
                    publishedAt = it.publishedAt
                )
            }

        }
    }

    suspend fun loadNews()
    {
        return  withContext(Dispatchers.IO)
        {
            try {
                refreshDb()
            }
            catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (hotDao.getAll().isEmpty())
                            throw Exception(
                                "Something went wrong. " +
                                        "We have no data.")
                    }
                    else -> throw e
                }
            }

        }
    }
    private suspend fun refreshDb()
    {
        val newsApiClient = NewsApiClient("d308291ce2574e1dbecbf555d6367707")

        newsApiClient.getTopHeadlines(
            TopHeadlinesRequest.Builder()
                .language("en")
                .category("technology")
                .build(),
            object : NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse?) {

                    val   remoteNews = response?.articles?.map {
                        LocalNews(
                            Nid = null,
                            source_name = it.source?.name ,
                            source_id = it.source?.id ?: "0",
                            author = it.author ?: "unknown",
                            title = it.title,
                            description = it.description,
                            urlToImage = it.urlToImage,
                            url = it.url,
                            publishedAt = it.publishedAt,

                            )
                    }


                    runBlocking { hotDao.deleteAllNews() }

                    runBlocking {
                        if (remoteNews != null) {
                            hotDao.addAll(remoteNews)
                        }
                    }

                }

                override fun onFailure(throwable: Throwable?) {
                    println(throwable?.message)
                }
            }
        )







    }
}



suspend fun makeRequest(url: String, onSuccess: (String?) -> Unit = {}, onFailure: (String?) -> Unit = {}) = withContext(Dispatchers.IO) {

    // Create an instance of the OkHttpClient class.
    val client = OkHttpClient()

    // Create a Request object with the URL of the resource you want to fetch.
    val request = Request.Builder()
        .url(url)
        .build()

    try {
        // Call the execute() method on the OkHttpClient object to send the request.
        val response = client.newCall(request).execute()

        // Handle the response from the server.
        if (response.isSuccessful) {
            // The request was successful.
            val body = response.body?.string()
            onSuccess(body)

        } else {
            // The request failed.
            val errorBody = response.body?.string()
            onFailure(errorBody)
        }

    } catch (e: IOException) {
        // An error occurred while sending the request.
        onFailure(e.message)
    }
}


suspend fun makeDeleteRequest(url: String, onSuccess: (String?) -> Unit = {}, onFailure: (String?) -> Unit = {}) = withContext(Dispatchers.IO) {

    // Create an instance of the OkHttpClient class.
    val client = OkHttpClient()

    // Create a Request object with the URL of the resource you want to delete.
    val request = Request.Builder()
        .url(url)
        .delete()
        .build()

    try {
        // Call the execute() method on the OkHttpClient object to send the request.
        val response = client.newCall(request).execute()

        // Handle the response from the server.
        if (response.isSuccessful) {
            // The request was successful.
            val body = response.body?.string()
            onSuccess(body)

        } else {
            // The request failed.
            val errorBody = response.body?.string()
            onFailure(errorBody)
        }
    } catch (e: Exception) {
        // The request encountered an exception.
        onFailure(e.message)
    }
}


suspend fun makePostRequest(
    endpoint: String,
    json: String,
    onSuccess: (String?) -> Unit = {},
    onFailure: (String?) -> Unit = {  }
) = withContext(Dispatchers.IO) {
    val client = OkHttpClient()

    val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://${getServerIp(HotApplication.getAppContext())}:8080/$endpoint")
        .post(requestBody)
        .build()

    try {
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        if (response.isSuccessful) {
            onSuccess(body)
        } else {
            onFailure(body)
        }
    } catch (e: IOException) {
        onFailure( e.message)
    }
}
