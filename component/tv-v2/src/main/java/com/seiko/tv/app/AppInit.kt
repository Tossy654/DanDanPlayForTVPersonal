package com.seiko.tv.app

import android.app.Application
import android.content.res.Configuration
import com.seiko.common.app.InitComponent
import org.koin.core.context.loadKoinModules

class AppInit : InitComponent {
    override fun onCreate(application: Application) {
        loadKoinModules(appModule)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

    override fun onLowMemory() {

    }
}