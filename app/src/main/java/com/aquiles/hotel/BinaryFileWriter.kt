package com.aquiles.hotel

import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class BinaryFileWriter(private val outputStream: OutputStream) : AutoCloseable{
   var onProgress : (Double) -> Unit = {}


    fun write(inputStream: InputStream?,length :Double): Long {
        BufferedInputStream(inputStream).use { input ->
            val dataBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var readBytes: Int
            var totalBytes: Long = 0
            while (input.read(dataBuffer).also { readBytes = it } != -1) {
                totalBytes += readBytes.toLong()
               onProgress(totalBytes / length * 100.0);
                outputStream.write(dataBuffer, 0, readBytes)
            }
            return totalBytes
        }
    }


     override fun close() {

    }

}


