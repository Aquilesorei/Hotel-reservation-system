package com.aquiles.hotel.libs.newsapilib.models.request


class TopHeadlinesRequest private constructor(builder: Builder) {
    val category: String?
    val sources: String?
    val q: String?
    val pageSize: String?
    val page: String?
    val country: String?
    val language: String?

    init {
        this.category = builder.category
        sources = builder.sources
        q = builder.q
        pageSize = builder.pageSize
        page = builder.page
        country = builder.country
        language = builder.language
    }

    class Builder {
        var q: String? = null
        var category: String? = null
        var sources: String? = null
        var country: String? = null
        var language: String? = null
        var pageSize: String? = null
        var page: String? = null
        fun q(q: String?): Builder {
            this.q = q
            return this
        }

        fun category(category: String?): Builder {
            this.category = category
            return this
        }

        fun sources(sources: String?): Builder {
            this.sources = sources
            return this
        }

        fun country(country: String?): Builder {
            this.country = country
            return this
        }

        fun language(language: String?): Builder {
            this.language = language
            return this
        }

        fun pageSize(pageSize: Int): Builder {
            this.pageSize = pageSize.toString()
            return this
        }

        fun page(page: Int): Builder {
            this.page = page.toString()
            return this
        }

        fun build(): TopHeadlinesRequest {
            return TopHeadlinesRequest(this)
        }
    }
}