package com.aidannemeth.weatherly.feature.common.dagger

import com.aidannemeth.weatherly.feature.common.domain.coroutines.AppScope
import com.aidannemeth.weatherly.feature.common.domain.coroutines.DefaultDispatcher
import com.aidannemeth.weatherly.feature.common.domain.coroutines.IODispatcher
import com.aidannemeth.weatherly.feature.common.domain.coroutines.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureCommonModule {
    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    @AppScope
    fun provideAppScope(@DefaultDispatcher dispatcher: CoroutineDispatcher) =
        CoroutineScope(dispatcher)
}
