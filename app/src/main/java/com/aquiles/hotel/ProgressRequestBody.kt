package com.aquiles.hotel



import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.buffer
import okio.source
import java.io.File



class ProgressRequestBody(
    private val file: File,
    private val contentType: MediaType,
    private val progressListener: ((Long, Long, Int) -> Unit)? = null
) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType
    }

    override fun contentLength(): Long {
        return file.length()
    }

    override fun writeTo(sink: BufferedSink) {
        val source = file.source().buffer()
        var totalBytesWritten: Long = 0

        while (true) {
            val bytesRead = source.read(sink.buffer, SEGMENT_SIZE)
            if (bytesRead == -1L) break

            sink.buffer.writeAll(source.buffer)
            sink.flush()

            totalBytesWritten += bytesRead
            progressListener?.invoke(totalBytesWritten, contentLength(),
                ((totalBytesWritten  / contentLength()) *100).toInt()
            )
        }

        source.close()
    }

    companion object {
        private const val SEGMENT_SIZE = 8 *1024 *1024L
    }
}