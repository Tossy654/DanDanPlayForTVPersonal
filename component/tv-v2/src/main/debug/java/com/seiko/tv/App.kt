package com.seiko.tv

import android.app.Application
import android.content.res.Configuration
import com.seiko.common.app.AppDelegate
import com.seiko.common.app.AppSetupDelegate
import com.seiko.common.di.moshiModule
import com.seiko.common.di.networkModule
import com.seiko.tv.app.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(), AppDelegate by AppSetupDelegate() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
        }
        setupApplication()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChanged(newConfig)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        clearOnLowMemory()
    }
}