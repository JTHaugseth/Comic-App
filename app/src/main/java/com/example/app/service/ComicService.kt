package com.example.app.service

import com.example.app.models.Comic
import java.io.IOException

// 3rd party library Gson for JSON parsing
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

// 3rd party library OkHttp for HTTP requests
import okhttp3.*

class ComicService {
    // Initializing OkHttp and Gson
    private val client = OkHttpClient()
    private val gson = Gson()
    private val responseSize = 10

    // Including a Callback function that provides either a list of comics or an IOException when the function is complete
    fun loadComics(startComicNumber: Int, callback: (List<Comic>?, IOException?) -> Unit) {
        val comics = mutableListOf<Comic>()
        var responsesReceived = 0

        for (i in startComicNumber until startComicNumber + responseSize) {
            val request = Request.Builder()
                .url("https://xkcd.com/$i/info.0.json")
                .build()

            // Sends an asynchronous HTTP request
            client.newCall(request).enqueue(object : Callback {

                // Callback methods using synchronized-blocks to ensure they run on one thread
                override fun onFailure(call: Call, e: IOException) {
                    synchronized(this) {
                        responsesReceived++
                        if (responsesReceived == responseSize) {
                            callback(null, e)
                        }
                    }
                }

                // If the response is successful we use Gson for JSON-parsing
                override fun onResponse(call: Call, response: Response) {
                    response.body?.string()?.let {
                        synchronized(this) {
                            // If any item has a successful response but failed to parse we invoke an error callback.
                            try {
                                val comic = gson.fromJson(it, Comic::class.java)
                                comics.add(comic)
                                responsesReceived++
                                if (responsesReceived == responseSize) {
                                    callback(comics, null)
                                }
                            } catch (e: JsonSyntaxException) {
                                responsesReceived++
                                if (responsesReceived == responseSize) {
                                    callback(null, IOException("Invalid comic data"))
                                }
                            }
                        }
                    }
                }
            })
        }
    }
}