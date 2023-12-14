package com.aquiles.hotel.libs.newsapilib.models.request


class EverythingRequest private constructor(builder: Builder) {
    val q: String?
    val sources: String?
    val domains: String?
    val from: String?
    val to: String?
    val language: String?
    val sortBy: String?
    val pageSize: String?
    val page: String?

    init {
        q = builder.q
        sources = builder.sources
        domains = builder.domains
        from = builder.from
        to = builder.to
        language = builder.language
        sortBy = builder.sortBy
        pageSize = builder.pageSize
        page = builder.page
    }

    class Builder {
        var q: String? = null
        var sources: String? = null
        var domains: String? = null
        var from: String? = null
        var to: String? = null
        var language: String? = null
        var sortBy: String? = null
        var pageSize: String? = null
        var page: String? = null
        fun q(q: String?): Builder {
            this.q = q
            return this
        }

        fun sources(sources: String?): Builder {
            this.sources = sources
            return this
        }

        fun domains(domains: String?): Builder {
            this.domains = domains
            return this
        }

        fun from(from: String?): Builder {
            this.from = from
            return this
        }

        fun to(to: String?): Builder {
            this.to = to
            return this
        }

        fun language(language: String?): Builder {
            this.language = language
            return this
        }

        fun sortBy(sortBy: String?): Builder {
            this.sortBy = sortBy
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

        fun build(): EverythingRequest {
            return EverythingRequest(this)
        }
    }
}