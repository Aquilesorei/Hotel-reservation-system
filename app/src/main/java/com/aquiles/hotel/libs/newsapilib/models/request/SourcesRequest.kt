package com.aquiles.hotel.libs.newsapilib.models.request


class SourcesRequest private constructor(builder: Builder) {
    val category: String?
    val language: String?
    val country: String?

    init {
        this.category = builder.category
        language = builder.language
        country = builder.country
    }

    class Builder {
        var category: String? = null
        var country: String? = null
        var language: String? = null
        fun category(category: String?): Builder {
            this.category = category
            return this
        }

        fun language(language: String?): Builder {
            this.language = language
            return this
        }

        fun country(country: String?): Builder {
            this.country = country
            return this
        }

        fun build(): SourcesRequest {
            return SourcesRequest(this)
        }
    }
}