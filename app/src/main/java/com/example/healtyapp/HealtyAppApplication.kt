package com.example.healtyapp

import android.app.Application

class HealtyAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: HealtyAppApplication
            private set
            
        fun isInitialized(): Boolean = ::instance.isInitialized
    }
}
