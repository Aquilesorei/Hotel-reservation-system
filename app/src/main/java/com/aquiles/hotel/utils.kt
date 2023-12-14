package com.aquiles.hotel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aquiles.hotel.models.Client
import com.aquiles.hotel.models.Profile
import com.aquiles.hotel.models.Room
import com.google.gson.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class RoomListDeserializer : JsonDeserializer<List<Room>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Room> {
        val Rooms = mutableListOf<Room>()
        if (json is JsonArray) {
            for (jsonElement in json) {
                val room = context!!.deserialize<Room>(jsonElement, Room::class.java)
                Rooms.add(room)
            }
        }
        return Rooms
    }
}





class ClientListDeserializer : JsonDeserializer<List<Client>> {
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


 suspend  fun  signUp(profile: Profile,onSuccess: () -> Unit = {}, onFailure: () -> Unit = {} )  = withContext(Dispatchers.IO){


    val client = OkHttpClient()
     val json = Gson().toJson(profile)



     println(json)
    val requestBody = json
        .toRequestBody("application/json".toMediaTypeOrNull())
    val request = Request.Builder()
        .url("http://${getServerIp(HotApplication.getAppContext())}:8080/signup")
        .post(requestBody)
        .build()

    val response = client.newCall(request).execute()
    if (response.isSuccessful ) {
        onSuccess()

    }else{

        onFailure()
    }
}







 suspend fun postFile(file: File, fileName: String) = withContext(Dispatchers.IO){

    // Create the multipart request body
    val requestBody: RequestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
           fileName,
            file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        )
        .build()

    // Create the HTTP request with OkHttp
    val request: Request = Request.Builder()
        .url("http://${getServerIp(HotApplication.getAppContext())}:8080/upload")
        .post(requestBody)
        .build()

    // Create the OkHttp client and execute the request
    val client = OkHttpClient()
    val response = client.newCall(request).execute()

    // Handle the response as needed
    if (response.isSuccessful) {

        println(response.body?.string())
    }
     else{
         println("failed to uploed ${response.body?.string()}")
    }
}



fun  getServerIp(context: Context) :String?{
    val  file= File(context.cacheDir,"ip.txt")
    return  if (file.exists()) return  file.readText() else null
}

fun saveInCache(context: Context, code: String) {
    val file = File(context.cacheDir, "ip.txt")
    runCatching {
        file.createNewFile()
    }.onSuccess {
        file.writeText(code)
    }.onFailure {
        // Handle the error
    }
}

fun getFileFromAssets(context: Context, fileName: String): File? {
    val assetManager = context.assets
    return try {
        val inputStream = assetManager.open("Icons/$fileName.png")
        val file = File.createTempFile("prefix", fileName, context.cacheDir)
        inputStream.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: IOException) {
        Log.e("getFileFromAssets", "Error opening/copying asset file: $fileName", e)
        null
    }
}



suspend  fun downloadFile(url: String,onProgress : (Double) -> Unit) = withContext(Dispatchers.IO) {

      val desFile = File(HotApplication.getAppContext().cacheDir,url.substringAfterLast("/"))

    val client = OkHttpClient()

    val writer = BinaryFileWriter(desFile.outputStream())

    writer.onProgress= onProgress
    val binaryFileDownloader = BinaryFileDownloader(client,writer)
    binaryFileDownloader.download(url)
}


fun getProfilePicture(context: Context): Bitmap? {


    val profilePicture = getFromSharedPreference(context,"profilePicture","")

    profilePicture?.let {
        val desFile = File(context.cacheDir,it)

        return BitmapFactory.decodeFile(desFile.absolutePath)

    }

    return null
}



/**
This function first checks if the thumbnail is already cached in the `cachedir` directory.
If it is, it returns the cached thumbnail.
If not, it creates a new thumbnail, saves it to the cache directory, and returns it.
 */
fun getCachedThumbnail(context: Context ,filePath: String,fileName: String): Bitmap? {
    val cacheDir = context.cacheDir


    val cachedThumbnailPath = File(cacheDir, "thumb_${fileName}_imgs")
    val thumbnail: Bitmap?

    if (cachedThumbnailPath.exists()) {
        thumbnail = BitmapFactory.decodeFile(cachedThumbnailPath.path)
    } else {
        val options = BitmapFactory.Options().apply {
            inSampleSize = 8
        }

        val originalBitmap = BitmapFactory.decodeFile(filePath, options) ?: return null

        val thumbnailSize = 256
        thumbnail = ThumbnailUtils.extractThumbnail(originalBitmap, thumbnailSize, thumbnailSize)

        try {
            FileOutputStream(cachedThumbnailPath).use { out ->
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    return thumbnail
}

fun saveToSharedPreference(context: Context, key: String, value: String) {
    // Get the shared preferences
    val sharedPref = context.getSharedPreferences("hotel_preferences", Context.MODE_PRIVATE)

    // Create an editor to edit the shared preferences
    val editor = sharedPref.edit()

    // Put the value in the editor
    editor.putString(key, value)

    // Commit the changes
    editor.apply()
}

fun getFromSharedPreference(context: Context, key: String, defaultValue: String): String? {
    // Get the shared preferences
    val sharedPref = context.getSharedPreferences("hotel_preferences", Context.MODE_PRIVATE)

    // Get the value from the shared preferences
    return sharedPref.getString(key, defaultValue)
}

fun Context.showToast(message: String,length: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, length).show()
}



@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateStr: String): String {
    val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val date = LocalDateTime.parse(dateStr, inputFormatter)
    return date.format(outputFormatter)
}

