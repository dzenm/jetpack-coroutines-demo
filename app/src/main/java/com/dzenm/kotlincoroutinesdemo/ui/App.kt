package com.dzenm.kotlincoroutinesdemo.ui

import android.app.Application
import com.dzenm.helper.base.Helper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Helper.init(this)
        app = this
    }

    companion object {
        lateinit var app: App
    }
}