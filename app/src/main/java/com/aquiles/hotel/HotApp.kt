package com.aquiles.hotel


import android.app.Application
import android.content.Context


class HotApplication: Application() {
    init { app = this }
    companion object {
        private lateinit var app: HotApplication
        fun getAppContext(): Context =
            app.applicationContext
    }
}