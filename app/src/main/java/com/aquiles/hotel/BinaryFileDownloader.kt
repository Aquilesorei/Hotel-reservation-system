package com.aquiles.hotel


import okhttp3.*
import org.apache.commons.fileupload.FileUploadBase.CONTENT_LENGTH
import java.io.IOException
import java.util.*


class BinaryFileDownloader(private val client: OkHttpClient, writer: BinaryFileWriter) : AutoCloseable {
    private val writer: BinaryFileWriter

    init {
        this.writer = writer
    }

    fun download(url: String): Long {
        val request: Request =  Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        val responseBody: ResponseBody = response.body
            ?: throw IllegalStateException("Response doesn't contain a file")
        val length: Double = getResponseLength(response)
        return writer.write(responseBody.byteStream(), length)
    }


    private fun getResponseLength(response: Response): Double {
        return Objects.requireNonNull(response.header(CONTENT_LENGTH, "1"))?.toDouble() ?: 0.0
    }

    override fun close() {

    }
}


