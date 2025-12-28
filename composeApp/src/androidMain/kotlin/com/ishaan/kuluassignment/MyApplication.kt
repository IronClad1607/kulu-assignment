package com.ishaan.kuluassignment

import android.app.Application
import com.ishaan.kuluassignment.di.initKoin
import multiplatform.network.cmptoast.AppContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Enable show toast messages
        AppContext.apply { set(applicationContext) }

        // Initialize Koin
        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }
    }
}