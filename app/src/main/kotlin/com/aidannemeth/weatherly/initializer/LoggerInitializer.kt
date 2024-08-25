package com.aidannemeth.weatherly.initializer

import android.content.Context
import androidx.startup.Initializer
import com.aidannemeth.weatherly.BuildConfig
import timber.log.Timber

class LoggerInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
