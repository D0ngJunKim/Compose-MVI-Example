package com.yeogibook

import android.app.Application
import com.yeogibook.abcmm.presentation.util.ContextProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YeogiBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ContextProvider.setContext(this)
    }
}