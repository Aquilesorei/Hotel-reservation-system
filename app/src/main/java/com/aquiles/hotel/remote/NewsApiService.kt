package com.aquiles.hotel.remote

import android.util.Log
import com.aquiles.hotel.libs.newsapilib.NewsApiClient
import com.aquiles.hotel.libs.newsapilib.models.Article
import com.aquiles.hotel.libs.newsapilib.models.request.TopHeadlinesRequest
import com.aquiles.hotel.libs.newsapilib.models.response.ArticleResponse


class NewsApiService {

 private val newsApiClient = NewsApiClient("api token")

    var general = ArticleResponse()
    var articles  = listOf<Article>()

      fun loadLatestNews(  category :String) {
    newsApiClient.getTopHeadlines(
      TopHeadlinesRequest.Builder()
        .language("en")
        .category(category)
        .build(),
      object : NewsApiClient.ArticlesResponseCallback {
        override fun onSuccess(response: ArticleResponse?) {
            if (response != null) {
                general = response
            }
            if (response != null) {
                if (response.articles != null) {
                    articles = response.articles!!
                }
            }
        }

        override fun onFailure(throwable: Throwable?) {
            if (throwable != null) {
                println(throwable.message)
            }
        }
      }
    )

  }

  fun getLatestNews(keyword :String,category: String): ArticleResponse {
    var resp : ArticleResponse = ArticleResponse()
    newsApiClient.getTopHeadlines(
      TopHeadlinesRequest.Builder()
        .language("en")
        .category(category)
        .q(keyword)
        .build(),
      object : NewsApiClient.ArticlesResponseCallback {
        override fun onSuccess(response: ArticleResponse?) {
            if (response != null) {
                resp = response
            }
        }

        override fun onFailure(throwable: Throwable?) {
            if (throwable != null) {
                println(throwable.message)
            }
        }
      }
    )
    return resp
  }




}