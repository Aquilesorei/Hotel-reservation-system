package com.aquiles.hotel.libs.newsapilib.models.response

import com.aquiles.hotel.libs.newsapilib.models.Article


class ArticleResponse {
    var status: String? = null
    var totalResults = 0
    var articles: List<Article>? = null
}