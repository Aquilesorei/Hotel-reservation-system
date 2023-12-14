package com.aquiles.hotel.libs.newsapilib

import com.aquiles.hotel.libs.newsapilib.models.request.EverythingRequest
import com.aquiles.hotel.libs.newsapilib.models.request.SourcesRequest
import com.aquiles.hotel.libs.newsapilib.models.request.TopHeadlinesRequest
import com.aquiles.hotel.libs.newsapilib.models.response.ArticleResponse
import com.aquiles.hotel.libs.newsapilib.models.response.SourcesResponse
import com.aquiles.hotel.libs.newsapilib.network.APIClient.aPIService
import com.aquiles.hotel.libs.newsapilib.network.APIService
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

class NewsApiClient(private val mApiKey: String) {
    private var query: MutableMap<String?, String?>
    private val mAPIService: APIService

    init {
        mAPIService = aPIService
        query = HashMap()
        query["apiKey"] = mApiKey
    }

    //Callbacks
    interface SourcesCallback {
        fun onSuccess(response: SourcesResponse?)
        fun onFailure(throwable: Throwable?)
    }

    interface ArticlesResponseCallback {
        fun onSuccess(response: ArticleResponse?)
        fun onFailure(throwable: Throwable?)
    }

    private fun errMsg(str: String): Throwable {
        var throwable: Throwable? = null
        try {
            val obj = JSONObject(str)
            throwable = Throwable(obj.getString("message"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (throwable == null) {
            throwable = Throwable("An error occured")
        }
        return throwable
    }

    private fun createQuery(): MutableMap<String?, String?> {
        query = HashMap()
        query["apiKey"] = mApiKey
        return query
    }

    //Get Sources
    fun getSources(sourcesRequest: SourcesRequest, callback: SourcesCallback) {
        query = createQuery()
        query["category"] = sourcesRequest.category
        query["language"] = sourcesRequest.language
        query["country"] = sourcesRequest.country
        query.values.removeAll(setOf<Any?>(null))
        mAPIService.getSources(query)
            ?.enqueue(object : Callback<SourcesResponse?> {
                override fun onResponse(
                    call: Call<SourcesResponse?>,
                    response: Response<SourcesResponse?>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        callback.onSuccess(response.body())
                    } else {
                        try {
                            callback.onFailure(errMsg(response.errorBody()!!.string()))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<SourcesResponse?>, throwable: Throwable) {
                    callback.onFailure(throwable)
                }
            })
    }

    fun getTopHeadlines(
        topHeadlinesRequest: TopHeadlinesRequest,
        callback: ArticlesResponseCallback
    ) {
        query = createQuery()
        query["country"] = topHeadlinesRequest.country
        query["language"] = topHeadlinesRequest.language
        query["category"] = topHeadlinesRequest.category
        query["sources"] = topHeadlinesRequest.sources
        query["q"] = topHeadlinesRequest.q
        query["pageSize"] = topHeadlinesRequest.pageSize
        query["page"] = topHeadlinesRequest.page
        query.values.removeAll(setOf<Any?>(null))
        query.values.removeAll(setOf("null"))
        mAPIService.getTopHeadlines(query)
            ?.enqueue(object : Callback<ArticleResponse?> {
                override fun onResponse(
                    call: Call<ArticleResponse?>,
                    response: Response<ArticleResponse?>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        callback.onSuccess(response.body())
                    } else {
                        try {
                            callback.onFailure(errMsg(response.errorBody()!!.string()))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ArticleResponse?>, throwable: Throwable) {
                    callback.onFailure(throwable)
                }
            })
    }

    fun getEverything(everythingRequest: EverythingRequest, callback: ArticlesResponseCallback) {
        query = createQuery()
        query["q"] = everythingRequest.q
        query["sources"] = everythingRequest.sources
        query["domains"] = everythingRequest.domains
        query["from"] = everythingRequest.from
        query["to"] = everythingRequest.to
        query["language"] = everythingRequest.language
        query["sortBy"] = everythingRequest.sortBy
        query["pageSize"] = everythingRequest.pageSize
        query["page"] = everythingRequest.page
        query.values.removeAll(setOf<Any?>(null))
        query.values.removeAll(setOf("null"))
        mAPIService.getEverything(query)
            ?.enqueue(object : Callback<ArticleResponse?> {
                override fun onResponse(
                    call: Call<ArticleResponse?>,
                    response: Response<ArticleResponse?>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        callback.onSuccess(response.body())
                    } else {
                        try {
                            callback.onFailure(errMsg(response.errorBody()!!.string()))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ArticleResponse?>, throwable: Throwable) {
                    callback.onFailure(throwable)
                }
            })
    }
}