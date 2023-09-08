package com.aidannemeth.weatherly.core.common.network

import com.aidannemeth.weatherly.core.common.network.WeatherlyDispatchers.Default
import com.aidannemeth.weatherly.core.common.network.WeatherlyDispatchers.IO
import com.aidannemeth.weatherly.core.common.network.WeatherlyDispatchers.Main
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutineDispatchersModule {

    @Provides
    @Dispatcher(Main)
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @Provides
    @Dispatcher(Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    @Dispatcher(IO)
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
