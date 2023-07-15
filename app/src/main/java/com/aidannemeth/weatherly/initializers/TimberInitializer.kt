package com.aidannemeth.weatherly.initializers

import android.content.Context
import androidx.startup.Initializer
import com.aidannemeth.weatherly.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

@Suppress("unused")
class TimberInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
